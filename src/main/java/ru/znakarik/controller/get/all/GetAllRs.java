package ru.znakarik.controller.get.all;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import ru.znakarik.controller.Url;

import java.util.List;

@AllArgsConstructor
@Builder
@Jacksonized
@Getter
public class GetAllRs {
    private final List<Url> urls;
}
