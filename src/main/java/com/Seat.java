package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat {
    private final String location;
    private boolean isTaken = false;

    public Seat(String location) {
        this.location = location;
    }
}

