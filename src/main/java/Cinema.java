import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {
    private String name;
    private String address;
    private final Collection<Movie> movies = new HashSet<>();
    private final Program program = new Program();

    protected void addMovie(Movie movie){
        movies.add(movie);
    }

    protected void removeMovie(Movie movie){
        movies.add(movie);
    }
}
