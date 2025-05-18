package ru.uoles.debezium.db;

/**
 * debezium
 * Created by Intellij IDEA.
 * Developer: uoles (Kulikov Maksim)
 * Date: 18.05.2025
 * Time: 23:20
 */
public enum PostgreConnection {

    INSTANCE(new PostgreJdbcTemplate());

    private PostgreJdbcTemplate postgreJdbcTemplate;

    PostgreConnection(PostgreJdbcTemplate postgreJdbcTemplate) {
        this.postgreJdbcTemplate = postgreJdbcTemplate;
    }

    public PostgreJdbcTemplate getTemplate() {
        return postgreJdbcTemplate;
    }
}
