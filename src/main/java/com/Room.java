package com;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Room implements Comparable<Room> {
    private final String name;
    private ArrayList<Seat> seats;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Room room)) return false;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(Room screening) {
        return name.compareTo(screening.name);
    }
}
