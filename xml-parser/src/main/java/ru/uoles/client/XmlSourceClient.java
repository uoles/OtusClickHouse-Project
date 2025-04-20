package ru.uoles.client;

import lombok.AllArgsConstructor;
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

    private static final String URL = "https://cbr.ru/scripts/XML_daily.asp?date_req=23/01/2022.xml";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<XmlElementDto> getXml() {
        XmlDto response = restTemplate.getForObject(URL, XmlDto.class);

        List<XmlElementDto> result = new ArrayList<>();
        if (response != null) {
            result = response.getValute();
        }
        return result;
    }
}
