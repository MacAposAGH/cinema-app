package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.lang.System.out;

@Data
@AllArgsConstructor
public class Screening implements Comparable<Screening> {
    private LocalTime time;
    private LocalDate date;
    private Room room;
    private Movie movie;
    private Projection projection;

    private void checkIfCurrent() {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("This session is over!");
        }
    }

    private Reservation createReservation(boolean prePaid, Customer customer, Seat... seats) {
        checkIfCurrent();
        ArrayList<Seat> seatCollection =new ArrayList<>(Arrays.asList(seats));
        room.reservePlaces(seatCollection);
        var reservations = new Reservation(this, new TreeSet<>(seatCollection), prePaid);
        if (customer != null) {
            customer.addReservation(reservations);
        }
        printReservationMessage(prePaid, customer);
        return reservations;
    }

    private Reservation createReservation(boolean prePaid, Customer customer, String... seats) {
        return createReservation(prePaid, customer, Arrays.stream(seats).map(Seat::new).toArray(Seat[]::new));
    }

    private void printReservationMessage(boolean prePaid, Customer customer) {
        StringBuilder sb = new StringBuilder("Seats reservation successful!\n");
        if (prePaid) {
            sb.append("You have reserved seats but still need to buy a tickets, " +
                    "so it is advisable to arrive at the cinema 20 minutes before the screening starts.\n" +
                    "Reservation information were sent to your email address.");
        } else {
            sb.append("You have reserved seats and paid for tickets.\nThey were sent to your email address.");
        }
        if (customer != null) {
            sb.append(" You can also view your reservations and tickets on your user profile.");
        } else if (prePaid) {
            sb.append(" A staff member will authenticate you before you enter the proper cinema room.");
        }
        out.println(sb.append("\n"));
    }

    public Reservation reservePlaces(String... seats) {
        return createReservation(false, null, seats);
    }

    public Reservation reservePlaces(Seat... seats) {
        return createReservation(false, null, seats);
    }

    public Reservation reservePlaces(Customer customer, String... seats) {
        return createReservation(false, customer, seats);
    }

    public Reservation buyTickets(String... seats) {
        return createReservation(true, null, seats);
    }

    public Reservation buyTickets(Seat... seats) {
        return createReservation(true, null, seats);
    }

    public Reservation buyTickets(Customer customer, String... seats) {
        return createReservation(true, customer, seats);
    }

    public String getScreeningInfo(String... additionalInfo) {
        StringBuilder sb = new StringBuilder();
        String dateFormat = "%-8s";
        String format = dateFormat.formatted("");
        sb.append(String.format(dateFormat, time));
        sb.append(movie.title()).append("\n");
        sb.append(format).append(movie.runtime()).append(" min").append("\n");
        sb.append(format).append(String.join(", ", movie.genres())).append("\n");
        sb.append(format).append(projection).append(", ").append(room.name()).append("\n");
        for (String s : additionalInfo) {
            sb.append(format).append(s);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Screening screening)) return false;
        return Objects.equals(time, screening.time) && Objects.equals(date, screening.date) && Objects.equals(room, screening.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, date, room);
    }

    @Override
    public int compareTo(Screening other) {
        return Comparator.comparing(Screening::getDate)
                .thenComparing(Screening::getTime)
                .thenComparing(Screening::getRoom)
                .thenComparing(Screening::getMovie).compare(this, other);
    }

}
