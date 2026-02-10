package com;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {
    public static void main(String[] args) {
//        ArrayList<com.Movie> movies = generateMovies();
//        ArrayList<com.Seat> seats = generateSeats();
//        ArrayList<com.Room> rooms = generateRooms(seats);
//        generateScreenings(rooms, movies);

        fetchMoviesData();
    }

    public static ArrayList<String> loadTitles() {
        Path path = Path.of("src/main/resources/titles.txt");
        ArrayList<String> movies = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                movies.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        movies.forEach(System.out::println);
        return movies.stream().limit(15).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Movie> generateMovies() {
        Random random = new Random();
        List<String> genres = List.of(
                "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama", "Family",
                "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV com.Movie", "Thriller",
                "War", "Western"
        );
        ArrayList<Movie> movies = new ArrayList<>();
        for (String title : loadTitles()) {
//            com.Movie movie = new com.Movie(title, random.nextInt(120, 211),
//                    genres.get(random.nextInt(genres.size()))
//            );
//            movies.add(movie);
        }
        return movies;
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
                Seat seat = new Seat(rowSymbol, col, false);
                seat.setSeatClass(seatClass);
                seats.add(seat);
            }
        }
        return seats;
    }

    public static ArrayList<Room> generateRooms(ArrayList<Seat> seats) {
        List<String> roomNames = IntStream.range(1, 3 + 1).mapToObj(i -> "com.Room " + i).toList();
        ArrayList<Room> rooms = new ArrayList<>();
        for (String name : roomNames) {
            Room room = new Room(name, seats);
            rooms.add(room);
        }
        return rooms;
    }

    public static ArrayList<Screening> generateScreenings(ArrayList<Room> rooms, ArrayList<Movie> movies) {
        int roomsSize = rooms.size();
        int moviesSize = movies.size();

        int days = 10;
        int roomScreeningsPerDay = 4;

        int j = 0;
        for (int i = 0; i < roomScreeningsPerDay; i++) {
            System.out.print(i + " ");
            if (j > movies.size() - 1) {
                j = 0;
            }
            for (Room room : rooms) {
                int k = j % movies.size();
                if (j > movies.size() - 1 && movies.size() > rooms.size()) {
                    k += (movies.size() % rooms.size());
                }
                System.out.printf("%-47s", movies.get(k).getTitle());
                j++;
            }
            System.out.println();
        }

        return null;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonNode fetchMoviesPage(String uri) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzA0MDJmYWRhYjM2M2VmNzk1NzRlZTk0ZmQwMGJkMyIsIm5iZiI6MTcwOTE0NjgzNy45ODcsInN1YiI6IjY1ZGY4MmQ1MTQwYmFkMDE2MjkzZGQyOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.nw7jIpCeK0TpzT_qsCDMopzVoxetammAnQtyU5YNRT8")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return OBJECT_MAPPER.readTree(response.body());

//            Path read = Paths.get(uri);
//            String lines = Files.readString(read);
//            return OBJECT_MAPPER.readTree(lines);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void fetchMoviesData() {
        ArrayList<String> ids = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
//            "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=%s"
            JsonNode jsonNode = fetchMoviesPage(String.format(
                    "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=%s", i));
            jsonNode.get("results").forEach(r -> ids.add(r.get("id").toString()));
        }

        ArrayList<Movie> movies = new ArrayList<>();
        for (String id : ids) {
//            "https://api.themoviedb.org/3/movie/%s?language=en-US"
//            "src/main/resources/movie_%s.json"
            JsonNode jsonNode = fetchMoviesPage(String.format("https://api.themoviedb.org/3/movie/%s?language=en-US", id));

            Movie movie = OBJECT_MAPPER.readValue(jsonNode.toString(), Movie.class);
            for (JsonNode genre : jsonNode.get("genres")) {
                movie.getGenres().add(genre.get("name").stringValue());
            }
            movies.add(movie);
        }
        OBJECT_MAPPER.writeValue(new File("src/main/resources/movies.json"), movies);

    }

}