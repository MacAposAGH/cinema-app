package com;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeSet;

import static com.Projection.*;

public class Main {
    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();
        Cinema cinema1 = dataGenerator.generateCinema("Small Mall Move Hall",
                "321 Papier Street, Tinyfornia, 87-654", 1);
        Cinema cinema2 = dataGenerator.generateCinema("Monolith Cinema",
                "123 Main Street, New Yolk City, 45-678", 2);

        Customer customer = new Customer("John Doe", "johnDoe@gmail.com");



        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime time1 = LocalTime.of(10, 0);
        LocalTime time2 = LocalTime.of(18, 0);

        Screening screening1 = cinema1.findScreeningsByDateAndTime(tomorrow, time1).getFirst();
        Screening screening2 = cinema2.findScreeningsByDateAndTime(tomorrow, time2).getFirst();

        cinema1.printProgrammeForNextWeek();

        Seat seat1 = new Seat("G3");
        Seat seat2 = new Seat("G4");
        Seat seat3 = new Seat("G5");

        screening1.reservePlaces(customer, "G3", "G4", "G5");
        screening1.buyTickets(customer, "H3", "H4", "H5");

        screening2.reservePlaces("H3", "H4", "H5");
        screening2.buyTickets(seat1, seat2, seat3);

        customer.printReservations();
        TreeSet<Screening> vipScreenings = cinema1.findScreeningsByProjection(VIP);
        cinema1.updateScreenings(vipScreenings, s->s.setTime(time2));
        customer.printReservations();

        cinema2.printProgrammeForToday();
        TreeSet<Screening> _2dScreenings = cinema2.findScreeningsByProjection(_2D);
        cinema2.updateScreenings(_2dScreenings, s->s.setProjection(_3D));
        cinema2.printProgrammeForToday();

    }
}
