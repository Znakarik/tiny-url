package ru.znakarik.controller.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import ru.znakarik.controller.Url;

@AllArgsConstructor
@Builder
@Getter
//@Setter
@Jacksonized
public class CreateUrlRs {
    private final Url url;
    private final int statusCode;
}
