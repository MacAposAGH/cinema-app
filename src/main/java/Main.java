import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
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
        movies.forEach(System.out::println);
        return movies;
    }

    public static ArrayList<Movie> generateMovies() {
        Random random = new Random();
        List<String> genres = List.of(
                "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama", "Family",
                "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller",
                "War", "Western"
        );
        ArrayList<Movie> movies = new ArrayList<>();
        for (String title : loadTitles()) {
            Movie movie = new Movie(title, random.nextInt(120, 211),
                    genres.get(random.nextInt(genres.size())));
            movies.add(movie);
        }
        return movies;
    }

    public static ArrayList<Seat> generateSeats() {
        List<String> rows = List.of("A", "B", "C", "D", "E", "F", "A11Y");
        ArrayList<Seat> seats = new ArrayList<>();
        for (String row : rows) {
            for (int col = 0; col < 10; col++) {
                Seat seat = new Seat(row, col, false);
                switch (row) {
                    case "A", "B" -> seat.setSeatClass(SeatClass.PROMO);
                    case "C", "D" -> seat.setSeatClass(SeatClass.SUPER_PROMO);
                    case "E", "F" -> seat.setSeatClass(SeatClass.STANDARD);
                    case "G", "H" -> seat.setSeatClass(SeatClass.VIP);
                    case "A11Y" -> seat.setSeatClass(SeatClass.ACCESSIBLE);
                }
                seats.add(seat);
            }
        }
        return seats;
    }

    public static ArrayList<Room> generateRooms() {
        List<String> roomNames = List.of("Room 1", "Room 2", "Room 3", "Room 4", "Room 5");
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Seat> seats = generateSeats();
        for (String name : roomNames) {
            Room room = new Room(name, seats);
            rooms.add(room);
        }
        return rooms;
    }

    public static ArrayList<Room> generateScreenings() {
        return null;
    }


//    public static void loadMoviesData() {
//        Path read = Paths.get("src/main/resources/movies_data.json");
//        Path write = Paths.get("src/main/resources/movies.txt");
//        Path genres = Paths.get("src/main/resources/genres.json");
//        ObjectMapper objectMapper = new ObjectMapper();
//        ArrayList<String> titles = new ArrayList<>();
//        try {
//            String lines = Files.readString(read);
//            JsonNode jsonNode = objectMapper.readTree(lines);
//            jsonNode.forEach(s -> {
//                s.get("results").forEach(m -> {
//                    String title = m.get("title").toString();
//                    titles.add(title.substring(1, title.length() - 1));
//                });
//            });
//            objectMapper.readTree(genres).get("genres")
//                    .forEach(g -> System.out.print(g.get("name") + ", "));
//            Files.write(write, titles);
//        } catch (IOException e) {
//            System.err.println("File does not exits!");
//        }
//    }
}