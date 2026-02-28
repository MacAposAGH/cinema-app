package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record Customer(String name, String surname, String email, Collection<Reservation> reservations) {
    public Customer(String name, String surname, String email) {
        this(name, surname, email, new ArrayList<>());
    }

    public void addReservations(Collection<Reservation> reservations) {
        this.reservations.addAll(reservations);
    }

    public Collection<Reservation> findByProjectionReservation(Projection projection) {
        return reservations.stream()
                .filter(reservation -> reservation.screening().projection().equals(projection))
                .toList();
    }

}
