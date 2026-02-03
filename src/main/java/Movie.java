import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class Movie {
    private String title;
    private int runtime;
    private String genre;
    private static final Random random = new Random();
    private static final ArrayList<String> genres = new ArrayList<>(List.of(
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama", "Family",
            "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller",
            "War", "Western"
    ));

    public Movie(String title) {
        this.title = title;
        this.runtime = random.nextInt(120, 211);
        this.genre = genres.get(random.nextInt(genres.size()));
    }
}
