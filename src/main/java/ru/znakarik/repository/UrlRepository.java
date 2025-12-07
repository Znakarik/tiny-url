package ru.znakarik.repository;

import ru.znakarik.db.model.url.UrlPOJO;
import ru.znakarik.db.model.url.UrlRedirectPOJO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UrlRepository {
    Collection<UrlPOJO> getAll();

    Optional<UrlPOJO> findUrlById(String id);

    UrlPOJO findLongUrlByShortUrl(String shortUrl);

    String create(String id,
                  String longUrl,
                  String shortUrl,
                  String dateTime);

    void createNewRedirect(String redirectId, String urlId, String dateTime);

    List<UrlRedirectPOJO> getAllRedirectsByUrlId(String urlId);

    void delete(String id);
}
