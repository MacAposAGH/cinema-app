package com;

import java.util.List;

public record Movie(String title, int runtime, List<String> genres) implements Comparable<Movie> {

    @Override
    public int compareTo(Movie movie) {
        return title.compareTo(movie.title);
    }
}
