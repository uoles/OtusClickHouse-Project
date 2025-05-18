package ru.uoles.model;

import lombok.Data;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
@Data
public class ValuteDto {

    private String date;
    private String name;
    private String strId;
    private String numCode;
    private String charCode;
    private String nominal;
    private String value;
}
