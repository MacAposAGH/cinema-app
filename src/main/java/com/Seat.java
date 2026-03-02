package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat implements Comparable<Seat> {
    private final String location;
    private boolean isTaken = false;

    public Seat(String location) {
        this.location = location;
    }

    @Override
    public int compareTo(Seat other) {
        return location.compareTo(other.location);
    }
}

