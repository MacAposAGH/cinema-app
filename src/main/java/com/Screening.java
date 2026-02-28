package com;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.lang.System.out;

public record Screening(LocalTime time, LocalDate date, Room room, Movie movie, Projection projection)
        implements Comparable<Screening> {

    private void checkIfCurrent() {
        if (LocalDateTime.of(date, time).isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("This session is over!");
        }
    }

    private Collection<Reservation> createReservation(boolean prePaid, Customer customer, Seat... seats) {
        checkIfCurrent();
        List<Seat> seatCollection = Arrays.asList(seats);
        room.reservePlaces(seatCollection);
        var reservations = seatCollection.stream()
                .map(seat -> new Reservation(this, seat, prePaid, customer))
                .toList();
        if (customer != null) {
            customer.addReservations(reservations);
        }
        printMessage(prePaid, customer);
        return reservations;
    }

    private Collection<Reservation> createReservation(boolean prePaid, Customer customer, String... seats) {
        return createReservation(prePaid, customer, Arrays.stream(seats).map(Seat::new).toArray(Seat[]::new));
    }

    private void printMessage(boolean prePaid, Customer customer) {
        StringBuilder sb = new StringBuilder("Seats reservation successful!\n");
        if (prePaid) {
            sb.append("You have reserved seats but still need to buy a tickets, " +
                    "so it is advisable to arrive at the cinema 20 minutes before the screening starts.\n" +
                    "Reservation information were sent to your email address.");
        } else {
            sb.append("You have reserved seats and paid for tickets.\nThey were sent to your email address.");
        }

        if (customer != null) {
            sb.append("You can also view your reservations and tickets on your user profile.");
        } else if (prePaid) {
            sb.append(" A staff member will authenticate you before you enter the proper cinema room.");
        }
        out.println(sb);
    }


    Collection<Reservation> reservePlaces(String... seats) {
        return createReservation(false, null, seats);
    }

    Collection<Reservation> reservePlaces(Seat... seats) {
        return createReservation(false, null, seats);
    }

    Collection<Reservation> reservePlaces(Customer customer, String... seats) {
        return createReservation(false, customer, seats);
    }

    Collection<Reservation> buyTickets(String... seats) {
        return createReservation(true, null, seats);
    }

    Collection<Reservation> buyTickets(Seat... seats) {
        return createReservation(true, null, seats);
    }

    Collection<Reservation> buyTickets(Customer customer, String... seats) {
        return createReservation(true, customer, seats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Screening screening)) return false;
        return Objects.equals(time, screening.time) && Objects.equals(date, screening.date) &&
                Objects.equals(room, screening.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, date, room);
    }

    @Override
    public int compareTo(Screening other) {
        return Comparator.comparing(Screening::date)
                .thenComparing(Screening::time)
                .thenComparing(Screening::room).compare(this, other);
    }
}
