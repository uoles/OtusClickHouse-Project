package ru.uoles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 5:25
 */
@Data
@EqualsAndHashCode
@Entity
@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlElementDto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @XmlElement(name = "NumCode")
    private String numCode;

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "Nominal")
    private int nominal;

    @XmlElement(name = "Name")
    private String name;

    @Getter
    @XmlElement(name = "Value")
    @JsonIgnore
    @Transient
    private String srtValue;

    public double getDoubleValue() {
        double result = 0;
        if (StringUtils.isNotEmpty(srtValue)) {
            result = Double.parseDouble(srtValue.replace(",", "."));
        }
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("\n")
                .append(name).append(" (")
                .append("numCode=").append(numCode).append(", ")
                .append("charCode=").append(charCode).append(", ")
                .append("nominal=").append(nominal).append(", ")
                .append("value=").append(srtValue)
                .append(")").toString();
    }
}
