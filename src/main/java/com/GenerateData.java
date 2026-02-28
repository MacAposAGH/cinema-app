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
        List<Movie> movies = limitMovies(16);
//        Cinema monolithCinema = generateCinemas("Monolith Cinema",
//                "123 Main Street, New Yolk City, 45-678", movies, 8);
        Cinema smallMallMoveHall = generateCinemas("Small Mall Move Hall",
                "321 Papier Street, Tinyfornia, 87-654", movies, 4);
        Collection<Screening> screeningsByProjection = smallMallMoveHall.findScreeningsByProjection(Projection.VIP);
        LocalTime time = LocalTime.of(22, 0, 0);
        for (Screening screening : screeningsByProjection) {
            smallMallMoveHall.updateScreenings(screening, new Screening(time, screening.date(), screening.room(), screening.movie(), screening.projection()));
        }
        Collection<Screening> projection = smallMallMoveHall.findScreeningsByProjection(Projection.VIP);

    }

    public static Cinema generateCinemas(String name, String address, List<Movie> movies, int roomsNumber) {
        ArrayList<Room> rooms = generateRooms(roomsNumber);
        TreeSet<Screening> screenings = generateScreenings(rooms, movies);
        return new Cinema(name, address, screenings);
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
                Seat seat = new Seat(rowSymbol + col, seatClass);
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

    public static TreeSet<Screening> generateScreenings(List<Room> rooms, List<Movie> movies) {
        int roomScreeningsPerDay = 3;
        int days = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalTime openingHour = LocalTime.of(10, 0, 0);

        String format = "%-50s";
        String collect = " ".repeat(6) + rooms.stream().map(room -> String.format(format, room.name()))
                .collect(Collectors.joining());
        TreeSet<Screening> screenings = new TreeSet<>();
        int j = 0;
        for (int day = 0; day < days; day++) {
            Collections.shuffle(movies);
            LocalDate screeningDay = LocalDate.now().plusDays(day);
            StringBuilder sb = new StringBuilder(screeningDay.format(formatter)).append("\n").append(collect)
                    .append("\n");
            for (int screeningOfTheDay = 0; screeningOfTheDay < roomScreeningsPerDay; screeningOfTheDay++) {
                if (j > movies.size() - 1) {
                    j = 0;
                }
                LocalTime screeningTime = openingHour.plusHours(screeningOfTheDay * 3);
                sb.append(screeningTime).append(" ");
                for (int i = 0; i < rooms.size(); i++) {
                    Room room = rooms.get(i);
                    List<Projection> projections = List.of(Projection.values());
                    int k = j % movies.size();
                    if (j > movies.size() - 1 && movies.size() > rooms.size()) {
                        k += (movies.size() % rooms.size());
                    }
                    Screening screening = new Screening(screeningTime, screeningDay, room, movies.get(k),
                            projections.get(i % projections.size()));
                    sb.append(String.format(format, movies.get(k).title() + " " + screening.projection()));
                    screenings.add(screening);
                    j++;
                }
                sb.append("\n");
            }
            if (day < 1) {
                System.out.println(sb);
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

    public static ArrayList<Movie> limitMovies(int l) {
        try {
            return generateMovies().stream().limit(l).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Movie> generateMovies() throws IOException {
        String moviesPathString = "src/main/resources/movies.json";
        Path moviesPath = Path.of(moviesPathString);
        if (Files.exists(moviesPath)) {
            ArrayList<Movie> movies = OBJECT_MAPPER.readValue(Files.readString(moviesPath), new TypeReference<>() {
            });
            return movies.stream().limit(12).collect(Collectors.toCollection(ArrayList::new));
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

}