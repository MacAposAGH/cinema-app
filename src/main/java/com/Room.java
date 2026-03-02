package com;

import java.util.ArrayList;
import java.util.Collection;

public record Room(String name, ArrayList<Seat> seats) implements Comparable<Room> {

    public Room(String name) {
        this(name, new ArrayList<>());
    }

    void reservePlaces(Collection<Seat> seatsToReserve) {
        for (Seat seatToReserve : seatsToReserve) {
            Seat seat = seats.stream().filter(seatToReserve::equals).findFirst().orElse(null);
            if (seat == null) {
                throw new IllegalArgumentException("No such seats!");
            }
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
