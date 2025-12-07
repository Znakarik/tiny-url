package ru.znakarik.controller.get.single;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.znakarik.controller.Url;

@AllArgsConstructor
@Getter
@Builder
public class GetLongUrlRs {
    private final Url url;
}
