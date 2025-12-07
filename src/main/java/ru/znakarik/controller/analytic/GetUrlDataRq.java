package ru.znakarik.controller.analytic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetUrlDataRq {
    private final String dateFrom;
    private final String dateTo;
    private final String shortUrl;
}
