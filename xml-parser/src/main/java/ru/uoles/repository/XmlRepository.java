package ru.uoles.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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

    private static final String INSERT_DATA_SQL =
            """
                INSERT INTO valute_data( id, date, name, str_id, num_code, char_code, nominal, value )
                    VALUES( nextval('seq_valute_id'), :date, :name, :strId, :numCode, :charCode, :nominal, :value )
                    ON CONFLICT ( date, str_id ) DO NOTHING
            """;

    private static final String SELECT_LAST_DATA_SQL =
            """
                SELECT date
                FROM valute_data
                ORDER BY id DESC
                LIMIT 1
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

    public String selectLastDate() {
        List<String> result = jdbcTemplate.query(
                SELECT_LAST_DATA_SQL,
                (rs, rowNum) -> rs.getString("date")
        );
        return !CollectionUtils.isEmpty(result) ? result.get(0) : "01/01/2010";
    }
}
