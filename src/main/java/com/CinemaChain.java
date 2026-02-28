package com;


import java.util.ArrayList;
import java.util.Collection;

public record CinemaChain(String name, Collection<Cinema> cinemas) {

    public CinemaChain(String name) {
        this(name, new ArrayList<>());
    }

    public void addCinema(Cinema cinema) {
        cinemas.add(cinema);
    }
}
