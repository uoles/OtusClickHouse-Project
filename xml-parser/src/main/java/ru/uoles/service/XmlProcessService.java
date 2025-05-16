package ru.uoles.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNowDate() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        return format(day, "00") + "/" +
                format(month, "00") + "/" +
                year;
    }
}
