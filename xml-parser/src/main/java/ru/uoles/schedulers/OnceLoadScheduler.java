package ru.uoles.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.uoles.service.XmlProcessService;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 16.05.2025
 * Time: 10:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnceLoadScheduler {

    private boolean isExecuted = false;

    private final XmlProcessService xmlProcessService;

    @Scheduled(initialDelay = 3000, fixedDelay = 60000)
    private void execute() {
        try {
            if (!isExecuted) {
                isExecuted = true;

                String currentDate = xmlProcessService.getNowDate();
                log.info("--- OnceLoadScheduler. Get data to current date: {}", currentDate);

                xmlProcessService.processPeriod("01/01/2010", currentDate);
            }
        } catch (Exception e) {
            log.error("ERROR. Getting xml exception: {}", e.getMessage(), e);
        }
    }
}
