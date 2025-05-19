package ru.uoles.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
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

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "89.169.3.137:29093");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        // send data - asynchronous
        try (Producer<String, String> producer = new KafkaProducer<>(props)){
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("valute_topic", mapper.writeValueAsString(valute));
            Future<RecordMetadata> future = producer.send(producerRecord);
            RecordMetadata metadata = future.get();

            producer.flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
