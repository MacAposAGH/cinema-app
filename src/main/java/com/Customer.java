package com;

import java.util.Collection;
import java.util.TreeSet;

public record Customer(
        String name,
        String surname,
        String email,
        TreeSet<Reservation> reservations) {

    public Customer(String name, String surname, String email) {
        this(name, surname, email, new TreeSet<>());
    }

    public void addReservations(Collection<Reservation> reservations) {
        this.reservations.addAll(reservations);
    }

    public boolean hasScreening(Screening screening) {
        return reservations().stream().anyMatch(r -> r.getScreening().equals(screening));
    }

    public void updateReservation(Screening existing, Screening replacement) {
//        reservations().stream()
//                .filter(reservation -> reservation.getScreening().equals(existing))
//                .findFirst().
    }

    public void printReservations() {
        CinemaUtil.printProgramme(reservations,r -> r.getScreening().getDate(),
                r -> r.getScreening().getScreeningInfo(r.getSeat().getLocation()), "");
    }
}
