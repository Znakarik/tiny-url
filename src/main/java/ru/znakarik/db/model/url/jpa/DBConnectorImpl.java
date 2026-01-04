package ru.znakarik.db.model.url.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

@Slf4j
public class DBConnectorImpl implements DBConnector {
    @Value("${spring.datasource.url}")
    private String url;

    public <T> T execute(String sql, Function<ResultSet, T> processResultFunction) {
        log.debug("[execute statement] = {}", sql);
        Optional<T> result = executeSql(sql, processResultFunction);
        return result.orElse(null);
    }

    private <T> Optional<T> executeSql(String sql, Function<ResultSet, T> processResultFunction) {
        if (isVoid(sql)) {
            try (Connection conn = DriverManager.getConnection(url, new Properties())) {
                Statement statement = conn.createStatement();
                statement.execute(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        } else {
            try (Connection conn = DriverManager.getConnection(url, new Properties())) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                return Optional.ofNullable(processResultFunction.apply(resultSet));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isVoid(String sql) {
        return sql.startsWith("TRUNCATE") || sql.startsWith("insert") || sql.startsWith("INSERT");
    }
}
