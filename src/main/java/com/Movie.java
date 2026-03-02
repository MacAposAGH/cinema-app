package com;

import java.util.ArrayList;
import java.util.List;

public record Movie(String title, int runtime, List<String> genres) implements Comparable<Movie> {

    public Movie(String title) {
        this(title, 0, new ArrayList<>());
    }

    @Override
    public int compareTo(Movie movie) {
        return title.compareTo(movie.title);
    }
}
