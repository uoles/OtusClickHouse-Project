package ru.uoles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
@EnableJpaRepositories
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}