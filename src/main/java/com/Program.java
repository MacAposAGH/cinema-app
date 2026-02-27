package com;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;


@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Program {
    protected final Collection<Screening> screenings;

    protected void addScreening(Screening screening){
        screenings.add(screening);
    }

    protected void removeScreening(Screening screening){
        screenings.add(screening);
    }

    private Collection<Screening> filterScreeningsByDate(Predicate<LocalDate> predicate) {
        return screenings.stream().filter(s -> predicate.test(s.getDate())).toList();
    }

    protected Collection<Screening> getScreeningsForToday() {
        return filterScreeningsByDate(LocalDate.now()::isEqual);
    }

    protected Collection<Screening> getScreeningsForDate(LocalDate date) {
        return filterScreeningsByDate(date::isEqual);
    }

    protected Collection<Screening> getScreeningsForNextWeek() {
        LocalDate now = LocalDate.now();
        return filterScreeningsByDate((s) ->
                s.isAfter(now.minusDays(1)) &&
                s.isBefore(now.plusDays(7)));
    }

}
