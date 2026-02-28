package com;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Cinema(String name, String address, Set<Movie> movies, TreeSet<Screening> screenings, Set<Room> rooms,
                     Set<Customer> customers) {
    public Cinema(String name, String address, Set<Movie> movies, TreeSet<Screening> screenings, Set<Room> rooms) {
        this(name, address, movies, screenings, rooms, new HashSet<>());
        checkScreenings(movies, rooms);
    }

    public Cinema(String name, String address, TreeSet<Screening> screenings) {
        this(name, address, new HashSet<>(), screenings, new HashSet<>(), new HashSet<>());
        screenings.forEach(screening -> {
            movies.add(screening.movie());
            rooms.add(screening.room());
        });
    }

    private void checkScreenings(Collection<Movie> movies, Collection<Room> rooms) {
        Predicate<Screening> areScreeningsValid =
                screening -> movies.contains(screening.movie()) && rooms.contains(screening.room());
        if (screenings.stream().noneMatch(areScreeningsValid)) {
            throw new IllegalArgumentException("Can't assign one of the screening. " +
                    "No such movie or room in this cinema!");
        }
    }

    private Collection<Screening> filterScreenings(Predicate<Screening> predicate) {
        return screenings.stream().filter(predicate).toList();
    }

    Collection<Screening> findScreeningsForToday() {
        return filterScreenings(screening -> screening.date().isEqual(LocalDate.now()));
    }

    Collection<Screening> findScreeningsByDate(LocalDate date) {
        return filterScreenings(screening -> screening.date().isEqual(date));
    }

    Collection<Screening> findScreeningsForNextWeek() {
        LocalDate now = LocalDate.now();
        return filterScreenings(screening -> {
            LocalDate date = screening.date();
            return date.isAfter(now.minusDays(1)) && date.isBefore(now.plusDays(7));
        });
    }

    Collection<Screening> findScreeningsByProjection(Projection projection) {
        return filterScreenings(screening -> screening.projection().equals(projection));
    }

    void updateScreenings(Screening existing, Screening replacement) {
        if (!screenings.remove(existing)) {
            throw new IllegalArgumentException("No such screening in this cinema!");
        }
        if (screenings.contains(replacement)) {
            Screening existingScreening = screenings.stream().filter(replacement::equals).findFirst().orElse(null);
            screenings.remove(existingScreening);
            screenings.add(new Screening(existing.time(), existing.date(), existing.room(), existingScreening.movie(),
                    existingScreening.projection()));
        }
        screenings.add(replacement);
    }

}
