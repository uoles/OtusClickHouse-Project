package ru.uoles.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 5:25
 */
@Data
@EqualsAndHashCode
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDto implements Serializable {

    @XmlElement(name = "Valute")
    private List<XmlElementDto> valute;

    @XmlElement(name = "Date")
    private LocalDate date;

    @Override
    public String toString() {
        return new StringBuilder("XmlDto (")
                .append("valute=").append(valute).append(", ")
                .append("date=").append(date).append(", ")
                .append(")").toString();
    }
}
