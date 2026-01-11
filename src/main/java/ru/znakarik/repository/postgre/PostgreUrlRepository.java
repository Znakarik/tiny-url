package ru.znakarik.repository.postgre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;
import ru.znakarik.db.model.url.jpa.DBConnector;
import ru.znakarik.repository.ConvertFunctions;
import ru.znakarik.repository.UrlRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class PostgreUrlRepository implements UrlRepository {
    private final DBConnector dbConnector;

    @Override
    public Collection<UrlPOJO> getAllUrls() {
        try {
            return dbConnector.execute("SELECT * FROM urls;", ConvertFunctions.FROM_RESULT_SET_URLS_TO_URLS_DTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UrlPOJO> findUrlById(String id) {
        try {
            UrlPOJO execute = dbConnector.execute(
                    String.format("SELECT * FROM urls where id = '%s';", id),
                    ConvertFunctions.FROM_RESULT_SET_TO_URL
            );
            return Optional.ofNullable(execute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UrlPOJO> findLongUrlByShortUrl(String shortUrl) {
        String statement = String.format("SELECT * FROM urls where short_url = '%s'", shortUrl);
        try {
            UrlPOJO result = dbConnector.execute(statement, ConvertFunctions.FROM_RESULT_SET_TO_URL);
            return Optional.ofNullable(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String create(String id, String longUrl, String shortUrl, String dateTime) {
        String createStatement = String.format("INSERT INTO " +
                        "urls (id, short_url, long_url, create_date_time)" +
                        " VALUES ('%s', '%s', '%s', '%s');",
                id, shortUrl, longUrl, dateTime);
        try {
            dbConnector.execute(createStatement, resultSet -> null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public void createNewRedirect(String redirectId, String urlId, String dateTime) {
        String createStatement = String.format("INSERT INTO " +
                        "url_redirects (id, url_id, create_date_time)" +
                        " VALUES ('%s', '%s', '%s');",
                redirectId, urlId, dateTime);
        try {
            dbConnector.execute(createStatement, resultSet -> null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UrlRedirectPOJO> getAllRedirectsByUrlId(String urlId) {
        String statement = String.format("SELECT * FROM url_redirects" +
                " WHERE url_id = '%s';", urlId);

        try {
            List<UrlRedirectPOJO> execute = dbConnector.execute(statement, ConvertFunctions.FROM_RESULT_SET_TO_REDIRECTS);
            log.info("executeResul = " + execute);
            return execute;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUrl(String id) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void deleteRedirect(String redirectId) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void clearUrls() {
        String statement = "TRUNCATE TABLE urls;";
        try {
            dbConnector.execute(statement, resultSet -> null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearRedirects() {
        String statement = "TRUNCATE TABLE url_redirects;";
        try {
            dbConnector.execute(statement, resultSet -> null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<UrlRedirectPOJO> getAllRedirects() {
        throw new RuntimeException("Not implemented yet");
    }
}
