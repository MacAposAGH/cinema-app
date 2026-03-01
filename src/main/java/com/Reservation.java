package com;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Reservation implements Comparable<Reservation> {
    private Screening screening;
    private final Seat seat;
    private final boolean isPrepaid;

    @Override
    public int compareTo(Reservation reservation) {
        return screening.compareTo(reservation.screening);
    }
}
