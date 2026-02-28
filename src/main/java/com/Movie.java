package com;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = MovieDeserializer.class)
public class Movie implements Comparable<Movie> {
    private String title;
    private int runtime;
    private List<String> genres;

    public Movie(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Movie movie) {
        return title.compareTo(movie.title);
    }
}
