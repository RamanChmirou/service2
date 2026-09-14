package com.mpie.service2.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    SCIENCE_FICTION("Science-Fiction"),
    NAUKOWE("Naukowe"),
    OTHER("Other");

    private final String name;
}