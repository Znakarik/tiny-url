package ru.znakarik.controller.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@AllArgsConstructor
@Jacksonized
@Builder
@Getter
@Setter
public class CreateUrlRq implements Serializable {
    @JsonProperty("longUrl")
    private final String longUrl;
}
