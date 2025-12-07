package ru.znakarik.controller.analytic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@Builder
@Jacksonized
@Getter
public class GetUrlDataRs {
    private final int count;
    private final String longUrl;
    private final String shortUrl;
    private final String id;
}
