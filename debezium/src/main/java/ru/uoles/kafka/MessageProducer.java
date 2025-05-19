package ru.uoles.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
public class MessageProducer {

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
    }
}
