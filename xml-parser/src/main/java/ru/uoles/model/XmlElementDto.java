package ru.uoles.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.*;
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
@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlElementDto implements Serializable {

    @XmlAttribute(name = "ID")
    protected String strId;

    @XmlElement(name = "NumCode")
    private String numCode;

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "Nominal")
    private int nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
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
                .append(name).append("-").append(strId).append(" (")
                .append("numCode=").append(numCode).append(", ")
                .append("charCode=").append(charCode).append(", ")
                .append("nominal=").append(nominal).append(", ")
                .append("value=").append(srtValue)
                .append(")").toString();
    }
}
