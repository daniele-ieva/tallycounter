package com.github.danieleieva.data.records;

import java.util.UUID;

public record Category(UUID id, String category) {
    public static Category of(UUID id, String category) {
        return new Category(id, category);
    }
}
