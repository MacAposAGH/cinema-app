package com;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static com.Projection.*;

public class DataGenerator {

    private ArrayList<Seat> generateSeats() {
        ArrayList<Seat> seats = new ArrayList<>();
        int rows = 10;
        int cols = 10;

        for (int row = 0; row < rows; row++) {
            String rowSymbol = String.valueOf((char) (65 + row));
            for (int col = 0; col < cols; col++) {
                Seat seat = new Seat(rowSymbol + col);
                seats.add(seat);
            }
        }
        return seats;
    }

    private List<Room> generateRooms(int roomsNumber) {
        return IntStream.range(1, roomsNumber + 1)
                .mapToObj(i -> new Room("Room " + i, generateSeats()))
                .toList();
    }

    private List<Movie> generateMovies() {
        String moviesPathString = "src/main/resources/movies.json";
        Path moviesPath = Path.of(moviesPathString);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Movie> movies = objectMapper.readValue(Files.readString(moviesPath), new TypeReference<>() {
            });
            return movies.stream().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Screening> generateScreenings(List<Room> rooms, List<Movie> movies) {
        int days = 4;
        List<LocalTime> screeningHours = List.of(
                LocalTime.of(10, 0, 0),
                LocalTime.of(14, 0, 0),
                LocalTime.of(18, 0, 0));
        List<Projection> screeningProjections = List.of(VIP, _2D, IMAX);

        ArrayList<Screening> screenings = new ArrayList<>();
        for (int day = 0; day < days; day++) {
            int j = 0;
            Collections.shuffle(movies);
            LocalDate date = LocalDate.now().plusDays(day);
            for (int i = 0; i < screeningHours.size(); i++) {
                LocalTime screeningHour = screeningHours.get(i);
                for (Room room : rooms) {
                    Screening screening = new Screening(screeningHour, date, room, movies.get(j),
                            screeningProjections.get(i));
                    screenings.add(screening);
                    j = Math.min(j + 1, movies.size() - 1);
                }
            }
        }
        return screenings;
    }

    public Cinema generateCinema(String name, String address, int numberOfRums) {
        List<Room> rooms = generateRooms(numberOfRums);
        List<Movie> movies = generateMovies();
        Cinema cinema = new Cinema(name, address, new HashSet<>(rooms), new HashSet<>(movies));
        List<Screening> screenings = generateScreenings(rooms, new ArrayList<>(movies));
        screenings.forEach(cinema::addScreenings);
        return cinema;
    }
}
