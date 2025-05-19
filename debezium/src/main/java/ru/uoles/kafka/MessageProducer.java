package ru.uoles.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.uoles.model.ValuteDto;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
@Slf4j
@Component
public class MessageProducer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "89.169.3.137:9092");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "0");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // send data - asynchronous
        try {
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("valute_topic", mapper.writeValueAsString(valute));
            Future<RecordMetadata> future = producer.send(producerRecord);
            RecordMetadata metadata = future.get();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // flush data - synchronous
        producer.flush();
        // flush and close producer
        producer.close();
    }
}
