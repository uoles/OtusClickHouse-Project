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
public class DayLoadScheduler {

    private final XmlProcessService xmlProcessService;

    @Scheduled(cron = "0 12 * * * *")
    private void execute() {
        try {
            String currentDate = xmlProcessService.getNowDate();
            log.info("--- DayLoadScheduler. Get data to current date: {}", currentDate);

            xmlProcessService.processDate(currentDate);
        } catch (Exception e) {
            log.error("ERROR. Getting xml exception: {}", e.getMessage(), e);
        }
    }
}
