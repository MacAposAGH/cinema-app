package com;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {
    private String name;
    private String address;
    private Collection<Movie> movies = new HashSet<>();
    private Collection<Screening> screenings = new HashSet<>();

    private Collection<Screening> filterScreenings(Predicate<Screening> predicate) {
        return screenings.stream().filter(predicate).toList();
    }

    protected Collection<Screening> findScreeningsForToday() {
        return filterScreenings(screening -> screening.getDate().isEqual(LocalDate.now()));
    }

    protected Collection<Screening> findScreeningsByDate(LocalDate date) {
        return filterScreenings(screening -> screening.getDate().isEqual(date));
    }

    protected Collection<Screening> findScreeningsForNextWeek() {
        LocalDate now = LocalDate.now();
        return filterScreenings(screening -> {
            LocalDate date = screening.getDate();
            return date.isAfter(now.minusDays(1)) && date.isBefore(now.plusDays(7));
        });
    }

    protected Collection<Screening> findScreeningsByProjection(Projection projection) {
        return filterScreenings(screening -> screening.getProjection().equals(projection));
    }

}
