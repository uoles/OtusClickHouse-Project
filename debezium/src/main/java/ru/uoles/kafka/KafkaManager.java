package ru.uoles.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.stereotype.Component;
import ru.uoles.model.ValuteDto;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
@Slf4j
@Component
@AllArgsConstructor
public class KafkaManager {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MessageProducer messageProducer;

    public void addProcess(final Map<String, Object> data) {
        executor.execute(() -> send(data));
    }

    public void send(final Map<String, Object> dataAfter) {
        ValuteDto valute = new ValuteDto();
        valute.setId((Long) dataAfter.get("id"));
        valute.setDate((String) dataAfter.get("date"));
        valute.setName((String) dataAfter.get("name"));
        valute.setStrId((String) dataAfter.get("str_id"));
        valute.setNumCode((String) dataAfter.get("num_code"));
        valute.setCharCode((String) dataAfter.get("char_code"));
        valute.setNominal((String) dataAfter.get("nominal"));
        valute.setValue((String) dataAfter.get("value"));

        executor.execute(() -> {
            log.info("--- ADD. Row: {}", valute);
        });

        try {
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("valute_topic", mapper.writeValueAsString(valute));

            messageProducer.getProducer().send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        log.info("--- Received new metadata." +
                                " Topic:" + recordMetadata.topic() +
                                " Partition: " + recordMetadata.partition() +
                                " Offset: " + recordMetadata.offset() +
                                " Timestamp: " + recordMetadata.timestamp());
                    } else {
                        log.error("Error while producing", e);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
