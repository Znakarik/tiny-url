package ru.znakarik.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@Builder
@Jacksonized
@Getter
public class Url {
    private final String id;
    private final String shortUrl;
    private final String longUrl;
}
