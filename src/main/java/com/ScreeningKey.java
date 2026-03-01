package com;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public record ScreeningKey(LocalTime time, LocalDate date, Room room) implements Comparable<ScreeningKey> {
    public ScreeningKey(Screening screening) {
        this(screening.getTime(), screening.getDate(), screening.getRoom());
    }

    @Override
    public int compareTo(ScreeningKey other) {
        return Comparator.comparing(ScreeningKey::date)
                .thenComparing(ScreeningKey::time)
                .thenComparing(ScreeningKey::room).compare(this, other);
    }
}
