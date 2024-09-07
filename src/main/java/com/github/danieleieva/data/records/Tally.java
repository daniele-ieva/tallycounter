package com.github.danieleieva.data.records;

import java.util.UUID;

public record Tally(String name, UUID category, Integer tally) {
    public static Tally of(String name, UUID category, Integer tally) {
        return new Tally(name, category, tally);
    }
}
