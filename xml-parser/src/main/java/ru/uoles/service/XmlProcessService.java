package ru.uoles.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.uoles.client.XmlSourceClient;
import ru.uoles.model.XmlElementDto;
import ru.uoles.repository.XmlRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.apache.commons.lang3.time.DateFormatUtils.format;

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

    public void processPeriod(final String fromDate, final String toDate) {
        List<String> list = getDatesBetween(fromDate, toDate);

        for (String date : list) {
            processDate(date);
        }
    }

    public List<String> getDatesBetween(String fromDate, String toDate) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDate = LocalDate.parse(fromDate, formatter);
        LocalDate endDate =  LocalDate.parse(toDate, formatter);

        for(LocalDate date = startDate; !endDate.isBefore(date); date = date.plusDays(1)){
            dates.add(date.format(formatter));
        }
        return dates;
    }

    public void processDate(final String date) {
        List<XmlElementDto> list = xmlSourceClient.getXmlByDate(date);
        xmlRepository.insert(list, date);

        log.info("Process date: {}", date);
        sleep();
    }

    public String getLastDate() {
        return xmlRepository.selectLastDate();
    }

    public void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNowDate() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();

        log.info("--- Current date {}, {}, {}", day, month, year);

        return StringUtils.leftPad(String.valueOf(day), 2, "0") + "/" +
                StringUtils.leftPad(String.valueOf(month), 2, "0") + "/" +
                year;
    }
}
