package ru.uoles.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.uoles.client.XmlSourceClient;
import ru.uoles.model.XmlElementDto;
import ru.uoles.repository.XmlRepository;

import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.IntStream;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 5:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class XmlProcessService {

    private final XmlSourceClient xmlSourceClient;
    private final XmlRepository xmlRepository;

    private boolean isExecuted = false;

    public void getXml() {
        IntStream.range(2020, 2025).forEachOrdered(year -> {
            IntStream.range(1, 13).forEachOrdered(month -> {
                YearMonth yearMonthObject = YearMonth.of(year, month);
                int daysInMonth = yearMonthObject.lengthOfMonth();

                for (int day=1; day<=daysInMonth; day++) {
                    String date = format(day, "00") + "/" +
                            format(month, "00") + "/" +
                            year;

                    List<XmlElementDto> list = xmlSourceClient.getXmlByDate(date);
                    xmlRepository.insert(list, date);

                    log.info("date {}", date);
                    sleep();
                }
            });
        });
    }

    public void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String format(int value, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 60000)
    private void test() {
        try {
            if (!isExecuted) {
                isExecuted = true;
                getXml();
            }
        } catch (Exception e) {
            log.error("ERROR. Getting xml exception: {}", e.getMessage(), e);
        }
    }
}
