package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat {
    private String row;
    private int column;
    private boolean isTaken;
    private SeatClass seatClass;

    public Seat(String row, int column, boolean isTaken) {
        this.row = row;
        this.column = column;
        this.isTaken = isTaken;
    }
}

