package ru.znakarik.db.model.url;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@Getter
@ToString
public class UrlPOJO {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private final Date createDateTime;
    private final String id;
    private final String shortUrl;
    private final String longUrl;
    private final Integer redirects;
}
