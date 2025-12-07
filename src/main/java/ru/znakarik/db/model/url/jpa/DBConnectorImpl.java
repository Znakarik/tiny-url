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
        log.info("[execute statement] = {}", sql);
        Optional<ResultSet> result = executeSql(sql);
            if (result.isPresent()) {
                return processResultFunction.apply(result.get());
            } else return null;
    }

    private Optional<ResultSet> executeSql(String sql) {
        if (sql.startsWith("insert") || sql.startsWith("INSERT")) {
            execute(sql);
            return Optional.empty();
        } else {
            return Optional.ofNullable(executeWithResultSet(sql));
        }
    }

    private void execute(String sql) {
        Properties properties = new Properties();
        try (Connection conn = DriverManager.getConnection(url, properties)) {
            Class.forName("org.postgresql.Driver");
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet executeWithResultSet(String sql) {
        Properties properties = new Properties();
        try (Connection conn = DriverManager.getConnection(url, properties)) {
            Class.forName("org.postgresql.Driver");
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
