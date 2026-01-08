package ru.znakarik.controller.get.single;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Jacksonized
@Builder
public class GetLongUrlRq implements Serializable {
    private final String shortUrl;
}
