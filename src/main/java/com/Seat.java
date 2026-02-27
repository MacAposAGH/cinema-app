package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat {
    private String location;
    private boolean isTaken = false;
    private SeatClass seatClass;

    public Seat(String location) {
        this.location = location;
    }

    public Seat(String location,SeatClass seatClass ) {
        this.location = location;
        this.seatClass = seatClass;
    }



}

