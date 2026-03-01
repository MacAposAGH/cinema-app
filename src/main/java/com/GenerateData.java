package com;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GenerateData {
    public static void main(String[] args) {
        ArrayList<Movie> movies = limitMovies(16);
//        Cinema monolithCinema = generateCinemas("Monolith Cinema",
//                "123 Main Street, New Yolk City, 45-678", movies, 8);
        Cinema cinema2 = generateCinemas("Small Mall Move Hall",
                "321 Papier Street, Tinyfornia, 87-654", movies, 2);
        cinema2.printProgramme();
        TreeSet<Screening> projection = cinema2.findScreeningsByProjection(Projection.VIP);
        Customer customer = new Customer("John", "Doe", "john.doe@gmail.com");
        TreeSet<Screening> screeningsForToday = cinema2.findScreeningsForToday();
        screeningsForToday.getLast().buyTickets(customer, "A2");
        cinema2.updateScreenings(projection, d -> d.setTime(LocalTime.of(22, 0, 0)));
        cinema2.printProgramme();
        customer.printReservations();
    }


    public static Cinema generateCinemas(String name, String address, ArrayList<Movie> movies, int roomsNumber) {
        ArrayList<Room> rooms = generateRooms(roomsNumber);
        Cinema cinema = new Cinema(name, address, new TreeSet<>(rooms), new HashSet<>(movies));
        ArrayList<Screening> screenings = generateScreenings(cinema, rooms, movies);
        screenings.forEach(cinema::addScreenings);
        return cinema;
    }

    public static ArrayList<Seat> generateSeats() {
        ArrayList<Seat> seats = new ArrayList<>();
        int rows = 10;
        int cols = 10;

        int vipRows = rows - (int) (0.2 * rows);
        int standardRows = vipRows - (int) Math.round(0.3 * rows);
        int promoRows = standardRows - (int) Math.round(0.3 * rows);

        for (int row = 0; row < rows; row++) {
            String rowSymbol = String.valueOf((char) (65 + row));
            SeatClass seatClass = SeatClass.SUPER_PROMO;
            if (row >= vipRows) {
                seatClass = SeatClass.VIP;
            } else if (row >= standardRows) {
                seatClass = SeatClass.STANDARD;
            } else if (row >= promoRows) {
                seatClass = SeatClass.PROMO;
            }
            for (int col = 0; col < cols; col++) {
                Seat seat = new Seat(rowSymbol + col);
                seats.add(seat);
            }
        }
        return seats;
    }

    public static ArrayList<Room> generateRooms(int roomsNumber) {
        List<String> roomNames = IntStream.range(1, roomsNumber + 1).mapToObj(i -> "Room " + i).toList();
        ArrayList<Room> rooms = new ArrayList<>();
        for (String name : roomNames) {
            Room room = new Room(name, generateSeats());
            rooms.add(room);
        }
        return rooms;
    }

    public static List<Movie> generateMovies() throws IOException {
        String moviesPathString = "src/main/resources/movies.json";
        Path moviesPath = Path.of(moviesPathString);
        if (Files.exists(moviesPath)) {
            ArrayList<Movie> movies = OBJECT_MAPPER.readValue(Files.readString(moviesPath), new TypeReference<>() {
            });
            return movies.stream().limit(12).toList();
        }

        Path idsPath = Path.of("src/main/resources/ids.txt");
        ArrayList<String> ids = new ArrayList<>();
        if (Files.exists(idsPath)) {
            ids.addAll(Files.readAllLines(idsPath));
        } else {
            for (int i = 1; i < 5; i++) {
                JsonNode jsonNode = fetch(String.format(
                        "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=%s", i));
                jsonNode.get("results").forEach(r -> ids.add(r.get("id").toString()));
            }
            Files.write(idsPath, ids);
        }

        ArrayList<Movie> movies = new ArrayList<>();
        for (String id : ids) {
            JsonNode jsonNode = fetch(String.format("https://api.themoviedb.org/3/movie/%s?language=en-US", id));
            Movie movie = OBJECT_MAPPER.readValue(jsonNode.toString(), Movie.class);
            movies.add(movie);
        }

        OBJECT_MAPPER.writeValue(new File(moviesPathString), movies);
        return movies;
    }

    public static ArrayList<Movie> limitMovies(int l) {
        try {
            return generateMovies().stream().limit(l).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Screening> generateScreenings(Cinema cinema, ArrayList<Room> rooms, ArrayList<Movie> movies) {
        int roomScreeningsPerDay = 4;
        int days = 2;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalTime openingHour = LocalTime.of(10, 0, 0);

        String format = "%-50s";
        String collect = " ".repeat(6) + rooms.stream().map(room -> String.format(format, room.name()))
                .collect(Collectors.joining());
        ArrayList<Screening> screenings = new ArrayList<>();
        int j = 0;
        for (int day = 0; day < days; day++) {
            Collections.shuffle(movies);
            LocalDate date = LocalDate.now().plusDays(day);
            StringBuilder sb = new StringBuilder(date.format(formatter)).append("\n").append(collect)
                    .append("\n");
            for (int screeningOfTheDay = 0; screeningOfTheDay < roomScreeningsPerDay; screeningOfTheDay++) {
                if (j > movies.size() - 1) {
                    j = 0;
                }
                LocalTime time = openingHour.plusHours(screeningOfTheDay * 3);
                sb.append(time).append(" ");
                for (int i = 0; i < rooms.size(); i++) {
                    Room room = rooms.get(i);
                    List<Projection> projections = List.of(Projection.values());
                    int k = j % movies.size();
                    if (j > movies.size() - 1 && movies.size() > rooms.size()) {
                        k += (movies.size() % rooms.size());
                    }
                    Screening screening = new Screening(time, date, room, movies.get(k),
                            projections.get(screeningOfTheDay % projections.size()));
                    sb.append(String.format(format, movies.get(k).title() + " " + screening.getProjection()));
                    screenings.add(screening);
                    j++;
                }
                sb.append("\n");
            }
            if (day < 1) {
//                System.out.println(sb);
            }
        }
        return screenings;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonNode fetch(String uri) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("accept", "application/json")
                    .header("Authorization",
                            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzA0MDJmYWRhYjM2M2VmNzk1NzRlZTk0ZmQwMGJkMyIsIm5iZiI6MTcwOTE0NjgzNy45ODcsInN1YiI6IjY1ZGY4MmQ1MTQwYmFkMDE2MjkzZGQyOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.nw7jIpCeK0TpzT_qsCDMopzVoxetammAnQtyU5YNRT8")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return OBJECT_MAPPER.readTree(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}