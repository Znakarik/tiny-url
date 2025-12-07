package ru.znakarik.repository;

import lombok.extern.slf4j.Slf4j;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class ConvertFunctions {
    public static Function<ResultSet, List<UrlPOJO>> FROM_RESULT_SET_URLS_TO_URLS_DTO = resultSet -> {
        List<UrlPOJO> urlPOJOS = new ArrayList<>();
        try {
            while (resultSet.next()) {
                urlPOJOS.add(createUrlFromResultSet(resultSet));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return urlPOJOS;
    };

    public static Function<ResultSet, UrlPOJO> FROM_RESULT_SET_TO_URL = resultSet -> {
        try {
            if (resultSet.next()) {
                return createUrlFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public static Function<ResultSet, List<UrlRedirectPOJO>> FROM_RESULT_SET_TO_REDIRECTS = resultSet -> {
        List<UrlRedirectPOJO> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                UrlRedirectPOJO urlRedirectPOJO = fromResultSetToRedirect(resultSet);
                log.info("selected urlRedirectPOJO = " + urlRedirectPOJO);
                result.add(urlRedirectPOJO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    };

    private static UrlPOJO createUrlFromResultSet(ResultSet resultSet) {
        try {
            String shortUrl = resultSet.getString("short_url");
            String longUrl = resultSet.getString("long_url");
            String id = resultSet.getString("id");

            String createDateTime = resultSet.getString("create_date_time");
            try {
                return UrlPOJO.builder()
                        .id(id)
                        .shortUrl(shortUrl)
                        .longUrl(longUrl)
                        .createDateTime(UrlPOJO.DATE_FORMAT.parse(createDateTime))
                        .build();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static UrlRedirectPOJO fromResultSetToRedirect(ResultSet resultSet) {
        try {
            String id = resultSet.getString("id");
            String urlId = resultSet.getString("url_id");
            String createDateTime = resultSet.getString("create_date_time");

            return UrlRedirectPOJO.builder()
                    .id(id)
                    .urlId(urlId)
                    .redirectDate(UrlPOJO.DATE_FORMAT.parse(createDateTime))
                    .build();
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
