package ru.uoles.debezium.offset;

import io.debezium.config.Configuration;
import org.apache.kafka.common.utils.ThreadUtils;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.uoles.debezium.config.PropertiesConfig;
import ru.uoles.debezium.db.PostgreConnection;
import ru.uoles.debezium.db.PostgreJdbcTemplate;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 *
 * Implementation of OffsetBackingStore that saves data to database table.
 * Source: https://review.couchbase.org/c/kafka-connect-mongo/+/202601/4/debezium-storage/
 *              debezium-storage-jdbc/src/main/java/io/debezium/storage/jdbc/offset/JdbcOffsetBackingStore.java
 */
public class PostgreOffsetBackingStore implements OffsetBackingStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgreOffsetBackingStore.class);

    private PostgreOffsetBackingStoreConfig config;
    private ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();
    private ExecutorService executor;
    private PostgreJdbcTemplate postgreJdbcTemplate;

    private final AtomicInteger recordInsertSeq = new AtomicInteger(0);

    public PostgreOffsetBackingStore() {
    }

    public String fromByteBuffer(ByteBuffer data) {
        return (data != null) ? String.valueOf(StandardCharsets.UTF_8.decode(data.asReadOnlyBuffer())) : null;
    }

    public ByteBuffer toByteBuffer(String data) {
        return (data != null) ? ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8)) : null;
    }

    @Override
    public void configure(WorkerConfig config) {
        try {
            Configuration configuration = Configuration.from(config.originalsStrings());
            this.config = new PostgreOffsetBackingStoreConfig(configuration, config);
            this.postgreJdbcTemplate = PostgreConnection.INSTANCE.getTemplate();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to connect JDBC offset backing store: " + config.originalsStrings(), e);
        }
    }

    @Override
    public synchronized void start() {
        executor = Executors.newFixedThreadPool(1, ThreadUtils.createThreadFactory(
                this.getClass().getSimpleName() + "-%d", false));

        LOGGER.info("Starting PostgresJdbcOffsetBackingStore db '{}'", PropertiesConfig.getJdbcUrl());
        try {
            initializeTable();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create JDBC offset table: " + PropertiesConfig.getJdbcUrl(), e);
        }
        load();
    }

    private void initializeTable() throws SQLException {
        DatabaseMetaData dbMeta = postgreJdbcTemplate.getConnection().getMetaData();
        ResultSet tableExists = dbMeta.getTables(null, null, config.getFullTableName(), null);

        if (tableExists.next()) {
            return;
        }

        LOGGER.info("Creating table {} to store offset", config.getFullTableName());
        postgreJdbcTemplate.executeQuery(config.getTableCreate());
    }

    protected void save() {
        LOGGER.debug("Saving data to state table...");

        postgreJdbcTemplate.executeQuery(config.getTableDelete());

        for (Map.Entry<String, String> mapEntry : data.entrySet()) {
            Timestamp currentTs = new Timestamp(System.currentTimeMillis());
            String key = (mapEntry.getKey() != null) ? mapEntry.getKey() : null;
            String value = (mapEntry.getValue() != null) ? mapEntry.getValue() : null;

            postgreJdbcTemplate.updateQuery(
                    config.getTableInsert(),
                    new HashMap<>() {{
                        put("id", UUID.randomUUID().toString());
                        put("key", key);
                        put("value", value);
                        put("timestamp", currentTs);
                        put("pos", recordInsertSeq.incrementAndGet());
                    }}
            );
        }
    }

    private void load() {
        ConcurrentHashMap<String, String> tmpData = new ConcurrentHashMap<>();
        postgreJdbcTemplate.query(
                config.getTableSelect(),
                (rs, rowNum) -> tmpData.put(
                        rs.getString("offset_key"),
                        rs.getString("offset_val")
                )
        );
        data = tmpData;
    }

    private void stopExecutor() {
        if (executor != null) {
            executor.shutdown();
            // Best effort wait for any get() and set() tasks (and caller's callbacks) to complete.
            try {
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (!executor.shutdownNow().isEmpty()) {
                throw new ConnectException("Failed to stop PostgresJdbcOffsetBackingStore. Exiting without cleanly " +
                        "shutting down pending tasks and/or callbacks.");
            }
            executor = null;
        }
    }

    @Override
    public synchronized void stop() {
        stopExecutor();
        LOGGER.info("Stopped PostgresJdbcOffsetBackingStore");
    }

    @Override
    public Future<Void> set(final Map<ByteBuffer, ByteBuffer> values, final Callback<Void> callback) {
        return executor.submit(new Callable<>() {
            @Override
            public Void call() {
                for (Map.Entry<ByteBuffer, ByteBuffer> entry : values.entrySet()) {
                    if (entry.getKey() == null) {
                        continue;
                    }
                    data.put(fromByteBuffer(entry.getKey()), fromByteBuffer(entry.getValue()));
                }
                save();

                if (callback != null) {
                    callback.onCompletion(null, null);
                }
                return null;
            }
        });
    }

    @Override
    public Future<Map<ByteBuffer, ByteBuffer>> get(final Collection<ByteBuffer> keys) {
        return executor.submit(new Callable<Map<ByteBuffer, ByteBuffer>>() {
            @Override
            public Map<ByteBuffer, ByteBuffer> call() {
                Map<ByteBuffer, ByteBuffer> result = new HashMap<>();
                for (ByteBuffer key : keys) {
                    result.put(key, toByteBuffer(data.get(fromByteBuffer(key))));
                }
                return result;
            }
        });
    }
}
