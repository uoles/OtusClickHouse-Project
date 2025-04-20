package ru.uoles.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.uoles.model.XmlDto;
import ru.uoles.model.XmlElementDto;

import java.util.ArrayList;
import java.util.List;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 5:21
 */
@Component
public class XmlSourceClient {

    // date format 23/01/2022
    private static final String URL = "https://cbr.ru/scripts/XML_daily.asp?date_req=:date.xml";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<XmlElementDto> getXmlByDate(final String date) {
        XmlDto response = restTemplate.getForObject(URL.replace(":date", date), XmlDto.class);

        List<XmlElementDto> result = new ArrayList<>();
        if (response != null) {
            result = response.getValute();
        }
        return result;
    }
}
