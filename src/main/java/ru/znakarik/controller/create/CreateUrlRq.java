package ru.znakarik.controller.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class CreateUrlRq implements Serializable {
    private final String longUrl;
}
