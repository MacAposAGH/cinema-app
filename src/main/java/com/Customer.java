package com;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class Customer {
    private String username;
    private String email;
    private final TreeSet<Reservation> reservations = new TreeSet<>();
    @Getter(AccessLevel.NONE)
    private final TreeSet<Screening> screenings = new TreeSet<>();

    public Customer(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        this.screenings.add(reservation.screening());
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
