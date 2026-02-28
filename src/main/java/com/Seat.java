package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat {
    private final String location;
    private boolean isTaken = false;
    private final SeatClass seatClass;

    public Seat(String location) {
        this.location = location;
        this.seatClass = SeatClass.STANDARD;
    }

    public Seat(String location,SeatClass seatClass ) {
        this.location = location;
        this.seatClass = seatClass;
    }



}

