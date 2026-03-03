package com;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeSet;

import static com.Projection.*;

public class Main {
    public static void main(String[] args) {
//        Create seats, rooms, movies and screenings
        DataGenerator dataGenerator = new DataGenerator();

//        Create cinemas
        CinemaChain cinemaChain = new CinemaChain("Big Movie");
        Cinema cinema1 = dataGenerator.generateCinema("Small Mall Move Hall",
                "321 Papier Street, Tinyfornia, 87-654", 1);
        Cinema cinema2 = dataGenerator.generateCinema("Monolith Cinema",
                "123 Main Street, New Yolk City, 45-678", 2);
        cinemaChain.addCinema(cinema1);
        cinemaChain.addCinema(cinema2);

//        Print programme for next week
        cinema1.printProgrammeForNextWeek();

        Seat seat1 = new Seat("G3");
        Seat seat2 = new Seat("G4");
        Seat seat3 = new Seat("G5");

//        Create customer
        Customer customer = new Customer("John Doe", "johnDoe@gmail.com");

//        Create time of screenings
        LocalDate tomorrow = LocalDate.now().plusDays(2);
        LocalTime time1 = LocalTime.of(10, 0);
        LocalTime time2 = LocalTime.of(18, 0);

        Screening screening1 = cinema1.findScreeningsByDateAndTime(tomorrow, time1).getFirst();
        Screening screening2 = cinema2.findScreeningsByDateAndTime(tomorrow, time2).getFirst();

//        Reserve places and buy tickets with customer account.
        screening1.reservePlaces(customer, "G3", "G4", "G5");
        screening1.buyTickets(customer, "H3", "H4", "H5");

//        Reserve places and buy tickets without customer account.
        screening2.reservePlaces("H3", "H4", "H5");
        screening2.buyTickets(seat1, seat2, seat3);

//        Manage VIP projections
//        Check customer reservation
        customer.printReservations();
        TreeSet<Screening> vipScreenings = cinema1.findScreeningsByProjection(VIP);
        cinema1.updateScreenings(vipScreenings, s -> s.setTime(time2));
//        Check customer reservation after update
        customer.printReservations();

//        Print programme for today
        cinema2.printProgrammeForToday();
//        Update 2D projection to 3D
        TreeSet<Screening> _2dScreenings = cinema2.findScreeningsByProjection(_2D);
        cinema2.updateScreenings(_2dScreenings, s -> s.setProjection(_3D));
//        Print programme after update
        cinema2.printProgrammeForToday();

    }
}
