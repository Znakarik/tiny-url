package ru.znakarik.db.model.url.jpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public interface DBConnector {
    <T> T execute(String sql, Function<ResultSet, T> processResultFunction) throws SQLException;
}
