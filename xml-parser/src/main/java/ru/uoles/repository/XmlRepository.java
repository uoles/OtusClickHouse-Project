package ru.uoles.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.uoles.model.XmlElementDto;

import javax.transaction.Transactional;
import java.util.List;

/**
 * xml-parser
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 20.04.2025
 * Time: 6:44
 */
@Component
@AllArgsConstructor
public class XmlRepository  {

    private static final String INSERT_DATA_SQL = """
            INSERT INTO valute_data( c_date, c_name, c_str_id, c_num_code, c_char_code, c_nominal, c_value )
                VALUES( :date, :name, :strId, :numCode, :charCode, :nominal, :value )
                ON CONFLICT DO NOTHING
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public void insert(final List<XmlElementDto> rows, final String date) {
        MapSqlParameterSource[] params = rows.stream().map(param -> {
            MapSqlParameterSource paramValues = new MapSqlParameterSource();
            paramValues.addValue("date", date);
            paramValues.addValue("name", param.getName());
            paramValues.addValue("strId", param.getStrId());
            paramValues.addValue("numCode", param.getNumCode());
            paramValues.addValue("charCode", param.getCharCode());
            paramValues.addValue("nominal", param.getNominal());
            paramValues.addValue("value", param.getSrtValue());
            return paramValues;
        }).toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(INSERT_DATA_SQL, params);
    }
}
