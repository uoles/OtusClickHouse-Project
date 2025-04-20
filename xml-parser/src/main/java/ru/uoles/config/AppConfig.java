package ru.uoles.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 7:20
 */
@Configuration
@AllArgsConstructor
public class AppConfig {

    private final PropertiesConfig propertiesConfig;

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(propertiesConfig.getDriverClassName());
        ds.setUrl(propertiesConfig.getJdbcUrl());
        ds.setUsername(propertiesConfig.getUsername());
        ds.setPassword(propertiesConfig.getPassword());
        ds.setSchema(propertiesConfig.getSchema());
        return ds;
    }
}
