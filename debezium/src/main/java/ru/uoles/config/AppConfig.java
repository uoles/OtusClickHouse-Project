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
public class AppConfig {

    @Value("${debezium.snapshot.initial}")
    private Integer snapshotInitial;

    @Value("${kafka.bootstrap.server}")
    private String kafkaServer;
}