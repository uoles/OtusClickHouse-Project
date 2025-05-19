package ru.uoles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("id")
    private Long id;

    @JsonProperty("date")
    private String date;

    @JsonProperty("name")
    private String name;

    @JsonProperty("str_id")
    private String strId;

    @JsonProperty("num_code")
    private String numCode;

    @JsonProperty("char_code")
    private String charCode;

    @JsonProperty("nominal")
    private String nominal;

    @JsonProperty("value")
    private String value;
}
