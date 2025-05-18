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

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void send(final Map<String, Object> dataAfter) {
        final ValuteDto valute = mapper.convertValue(dataAfter, ValuteDto.class);

        executor.execute(() -> {
            log.info("--- ADD. Row: {}", valute);
        });
    }
}
