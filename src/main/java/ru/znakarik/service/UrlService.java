package ru.znakarik.service;

import ru.znakarik.db.model.url.UrlPOJO;

import java.util.List;

public interface UrlService {
    UrlPOJO create(String longUrl);

    List<UrlPOJO> getAll();

    UrlPOJO getByShortUrl(String shortUrl);
    UrlPOJO getById(String id);
    UrlPOJO updateUrlRedirect(String urlId);
}
