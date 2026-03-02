package com;

import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

public record Customer(
        String username,
        String email,
        TreeSet<Reservation> reservations) {

    public Customer(String username, String email) {
        this(username, email, new TreeSet<>());
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void printReservations() {
        Cinema.printProgramme(reservations,
                r -> r.screening().getDate(),
                r -> r.screening().getScreeningInfo(
                        r.seats().stream()
                                .map(Seat::getLocation)
                                .collect(Collectors.joining(", ")) + "\n"),
                "");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(username, customer.username) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
