package ru.uoles.debezium;

import com.google.common.collect.ImmutableMap;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.uoles.debezium.config.PropertiesConfig;
import ru.uoles.debezium.constants.SlotConstants;
import ru.uoles.debezium.db.PostgreConnection;
import ru.uoles.debezium.db.PostgreJdbcTemplate;
import ru.uoles.kafka.MessageProducer;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.debezium.data.Envelope.FieldName.*;
import static io.debezium.data.Envelope.Operation;
import static java.util.stream.Collectors.toMap;
import static ru.uoles.debezium.constants.SlotConstants.SLOT_NAME;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */

@Slf4j
@Component
public class DebeziumListener {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());

    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final PostgreJdbcTemplate postgreJdbcTemplate = PostgreConnection.INSTANCE.getTemplate();
    private final MessageProducer messageProducer;

    @Autowired
    public DebeziumListener(Configuration customerConnectorConfiguration, MessageProducer messageProducer) {
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(customerConnectorConfiguration.asProperties())
                .using(this.getClass().getClassLoader())
                .notifying(this::handleChangeEvent)
                .build();

        this.messageProducer = messageProducer;
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        try {
            SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
            Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

            if (Objects.nonNull(sourceRecordChangeValue)) {
                Operation operation = Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

                if (!operation.equals(Operation.READ)) {
                    Map<String, Object> dataAfter = getData((Struct) sourceRecordChangeValue.get(AFTER));

                    messageProducer.send(dataAfter);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("ERROR. Processing database event exception: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getData(final Struct struct) {
        Map<String, Object> map = new HashMap<>();
        if (Objects.nonNull(struct)) {
            map = struct.schema().fields().stream()
                    .filter(o -> Objects.nonNull(struct.get(o.name())))
                    .collect(toMap(Field::name, o -> struct.get(o.name())));
        }
        return map;
    }

    private boolean slotIsNotActive() {
        List<Boolean> result = postgreJdbcTemplate.query(
                SlotConstants.SLOT_STATUS_SELECT,
                ImmutableMap.of(SlotConstants.SLOT_NAME_PARAM, SLOT_NAME),
                (rs, rowNum) -> rs.getBoolean(SlotConstants.SLOT_ACTIVE_COLUMN)
        );

        return !CollectionUtils.isEmpty(result) && !result.get(0);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (Objects.nonNull(this.debeziumEngine)) {
            this.debeziumEngine.close();
            log.info("--- DebeziumListener stopped.");
        }
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 15000)
    private void execute() {
        int count = this.executor.getActiveCount();
        if (count == 0 && slotIsNotActive()) {
            executor.execute(debeziumEngine);
            log.info("--- DebeziumListener started");
        }
    }
}