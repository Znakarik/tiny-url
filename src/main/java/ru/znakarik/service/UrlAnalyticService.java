package ru.znakarik.service;

import java.util.Date;

public interface UrlAnalyticService {
    int getAllRedirectsByShortUrlAndDate(String shortUrl, Date from, Date to);

}
