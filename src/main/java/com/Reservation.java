package com;

import java.util.TreeSet;

public record Reservation(
        Screening screening,
        TreeSet<Seat> seats,
        boolean isPrepaid) implements Comparable<Reservation> {

    @Override
    public int compareTo(Reservation reservation) {
        return screening.compareTo(reservation.screening);
    }
}
