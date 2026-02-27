package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
}
