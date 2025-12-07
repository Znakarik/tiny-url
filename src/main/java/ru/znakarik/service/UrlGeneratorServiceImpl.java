package ru.znakarik.service;

import org.springframework.beans.factory.annotation.Value;

public class UrlGeneratorServiceImpl implements UrlGeneratorService {
    @Value("${base.url}")
    private String baseUrl;
    @Value("${url.prefix}")
    private String prefix;

    @Override
    public String createShortUrl(String urlId) {
        return new StringBuilder()
                .append(baseUrl).append("/")
                .append(prefix)
                .append("?id=").append(urlId)
                .toString();
    }
}
