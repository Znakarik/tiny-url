package ru.znakarik.db.model.url;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class UrlRedirectPOJO implements Serializable {
    private final String id;
    private final String urlId;
    private final Date redirectDate;
}
