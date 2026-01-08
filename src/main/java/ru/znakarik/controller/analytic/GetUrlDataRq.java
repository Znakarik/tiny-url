package ru.znakarik.controller.analytic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class GetUrlDataRq implements Serializable {
    private final String dateFrom;
    private final String dateTo;
    private final String shortUrl;
}
