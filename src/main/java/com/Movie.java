package com;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = MovieDeserializer.class)
public record Movie(String title, int runtime, List<String> genres) implements Comparable<Movie> {

    public Movie(String title) {
        this(title, 0, new ArrayList<>());
    }

    @Override
    public int compareTo(Movie movie) {
        return title.compareTo(movie.title);
    }
}
