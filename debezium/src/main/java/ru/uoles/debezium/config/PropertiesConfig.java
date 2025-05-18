package ru.uoles.debezium.config;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
public class PropertiesConfig {

    private static Properties configuration = null;

    @SneakyThrows
    public static Properties getProperties() {
        if (Objects.isNull(configuration)) {
            try (InputStream inputStream = PropertiesConfig.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties")
            ) {
                configuration = new Properties();
                configuration.load(inputStream);
            }
        }
        return configuration;
    }

    public static String getJdbcUrl() {
        StringBuilder result = new StringBuilder();
        result.append("jdbc:postgresql://")
                .append(getHost())
                .append(":")
                .append(getPort())
                .append("/")
                .append(getDatabaseName());

        return result.toString();
    }

    public static String getHost() {
        return getProperties().getProperty("customer.datasource.host");
    }

    public static String getPort() {
        return getProperties().getProperty("customer.datasource.port");
    }

    public static String getUsername() {
        return getProperties().getProperty("customer.datasource.username");
    }

    public static String getPassword() {
        return getProperties().getProperty("customer.datasource.password");
    }

    public static String getSchema() {
        return getProperties().getProperty("customer.datasource.schema");
    }

    public static String getDatabaseName() {
        return getProperties().getProperty("customer.datasource.database");
    }
}
