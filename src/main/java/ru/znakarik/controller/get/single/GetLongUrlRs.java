package ru.znakarik.controller.get.single;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import ru.znakarik.controller.Url;

@AllArgsConstructor
@Getter
@Builder
@Jacksonized
public class GetLongUrlRs {
    private final Url url;
}
