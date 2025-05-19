package ru.uoles.debezium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

import static ru.uoles.debezium.constants.SlotConstants.PUBLICATION_NAME;
import static ru.uoles.debezium.constants.SlotConstants.SLOT_NAME;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
@Configuration
public class DebeziumConfig {

    @Bean
    public io.debezium.config.Configuration connector(Environment env) throws IOException {
        return io.debezium.config.Configuration.create()
                .with("name", "pgclick_postgres_connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage", "ru.uoles.debezium.offset.PostgreOffsetBackingStore")
                .with("offset.flush.interval.ms", "5000")
                .with("database.hostname", PropertiesConfig.getHost())
                .with("database.port", PropertiesConfig.getPort())
                .with("database.user", PropertiesConfig.getUsername())
                .with("database.password", PropertiesConfig.getPassword())
                .with("database.dbname", PropertiesConfig.getDatabaseName())
                .with("database.schema", PropertiesConfig.getSchema())
                .with("database.server.id", "10181")
                .with("database.server.name", "pgclick-postgres-db")
                .with("database.history", "io.debezium.relational.history.MemoryDatabaseHistory")
                .with("table.include.list", "pgclick.valute_data")
                .with("publication.autocreate.mode", "filtered")
                .with("plugin.name", "pgoutput")
                .with("publication.name", PUBLICATION_NAME)
                .with("slot.name", SLOT_NAME)
                .with("snapshot.mode", "initial")
                .build();
    }
}
