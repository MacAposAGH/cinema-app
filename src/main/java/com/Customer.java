package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
public class Customer {
    private String name;
    private String surname;
    private String email;
    private final Collection<Reservation> reservations = new ArrayList<>();

    public void addReservations(Collection<Reservation> reservations) {
        this.reservations.addAll(reservations);
    }

    public Collection<Reservation>  findByProjectionReservation(Projection projection) {
        return reservations.stream()
                    .filter(reservation -> reservation.getScreening().projection().equals(projection))
                    .toList();
    }

}
