package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
public class Room {
    private String name;
    private  ArrayList<Seat> seats ;

    protected void reservePlaces(Collection<Seat> seatsToReserve) {
        for (Seat seatToReserve : seatsToReserve) {
            int i = seats.indexOf(seatToReserve);
            if (i < 0) {
                throw new IllegalArgumentException("No such seat!");
            }
            Seat seat = seats.get(i);
            if (seat.isTaken()) {
                throw new IllegalArgumentException(
                        String.format("Seat with number %s is already taken!", seatToReserve.getLocation()));
            }
            seat.setTaken(true);
        }
    }
}
