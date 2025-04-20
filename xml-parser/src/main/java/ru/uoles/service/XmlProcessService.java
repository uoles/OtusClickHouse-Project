package ru.uoles.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.uoles.client.XmlSourceClient;
import ru.uoles.model.XmlElementDto;

import java.util.List;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 5:45
 */
@Slf4j
@Service
@AllArgsConstructor
public class XmlProcessService {

    private final XmlSourceClient xmlSourceClient;

    public void getXml() {
        List<XmlElementDto> list = xmlSourceClient.getXml();

        log.info("XML: {}", list);
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 15000)
    private void execute() {
        try {
            this.getXml();
        } catch (Exception e) {
            log.error("ERROR. Getting xml exception: {}", e.getMessage(), e);
        }
    }
}
