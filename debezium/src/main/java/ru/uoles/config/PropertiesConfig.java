package ru.uoles.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
@Data
@Component
public class PropertiesConfig {

    @Value("${customer.datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${customer.datasource.schema}")
    private String schema;

    @Value("${customer.datasource.username}")
    private String username;

    @Value("${customer.datasource.password}")
    private String password;

    @Value("${customer.datasource.driver-class-name}")
    private String driverClassName;
}