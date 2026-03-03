package com;

import java.util.Collection;

public record Movie(
        String title,
        int runtime,
        Collection<String> genres) implements Comparable<Movie> {

    @Override
    public int compareTo(Movie movie) {
        return title.compareTo(movie.title);
    }
}
