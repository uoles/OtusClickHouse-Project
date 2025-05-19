package ru.uoles.kafka;

import lombok.Getter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.uoles.config.AppConfig;

import java.util.Properties;

@Component
public class MessageProducer {

    @Getter
    private final Producer<String, String> producer;

    public MessageProducer(AppConfig appConfig) {
        Properties props = new Properties();
        props.put("bootstrap.servers", appConfig.getKafkaServer());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<String, String>(props);
    }
}
