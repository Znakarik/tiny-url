package ru.znakarik.controller.analytic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@AllArgsConstructor
@Jacksonized
@Builder
@Getter
public class GetUrlDataRq implements Serializable {
    private final String dateFrom;
    private final String dateTo;
    private final String shortUrl;
}
