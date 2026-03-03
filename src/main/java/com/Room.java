package com;

import java.util.ArrayList;

public record Room(
        String name,
        ArrayList<Seat> seats) implements Comparable<Room> {

    void reservePlaces(ArrayList<Seat> seatsToReserve) {
        for (Seat seatToReserve : seatsToReserve) {
            int indexOf = seats.indexOf(seatToReserve);
            if (indexOf < 0) {
                throw new IllegalArgumentException("No such seats!");
            }
            Seat seat = seats.get(indexOf);
            if (seat.isTaken()) {
                throw new IllegalArgumentException(
                        String.format("Seat with number %s is already taken!", seatToReserve.getLocation()));
            }
            seat.setTaken(true);
        }
    }

    @Override
    public int compareTo(Room screening) {
        return name.compareTo(screening.name);
    }
}
