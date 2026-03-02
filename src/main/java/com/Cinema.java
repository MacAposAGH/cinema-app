package com;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.System.out;

public record Cinema(
        String name,
        String address,
        HashSet<Room> rooms,
        HashSet<Movie> movies,
        TreeMap<ScreeningKey, Screening> screenings,
        HashSet<Customer> customers) {

    public Cinema {
        screenings.values().forEach(this::checkScreenings);
    }

    public Cinema(String name, String address, HashSet<Room> rooms, HashSet<Movie> movies) {
        this(name, address, rooms, movies, new TreeMap<>(), new HashSet<>());
    }

    void addScreenings(Screening screening) {
        checkScreenings(screening);
        screenings.put(new ScreeningKey(screening), screening);
    }

    private void checkScreenings(Screening screening) {
        if (!movies.contains(screening.getMovie()) || !rooms.contains(screening.getRoom())) {
            throw new IllegalArgumentException("Can't assign one of the screening. " +
                    "No such movie or room in this cinema!");
        }
    }

    void updateScreenings(Collection<Screening> set, Consumer<Screening> screeningUpdater) {
        for (Screening screening : set) {
            ScreeningKey oldScreeningKey = new ScreeningKey(screening);
            if (screenings.remove(oldScreeningKey) == null) {
                throw new IllegalArgumentException("No such screening in this cinema!");
            }
            screeningUpdater.accept(screening);
            ScreeningKey newScreeningKey = new ScreeningKey(screening);

            Screening existingScreening = screenings.get(newScreeningKey);
            if (existingScreening != null) {
                Screening updatedScreening = new Screening(
                        oldScreeningKey.time(), oldScreeningKey.date(), oldScreeningKey.room(),
                        existingScreening.getMovie(), existingScreening.getProjection());
                screenings.put(oldScreeningKey, updatedScreening);
            }
            screenings.put(newScreeningKey, screening);
        }
    }

    private TreeSet<Screening> filterScreenings(Predicate<Screening> predicate) {
        return screenings.values().stream().filter(predicate).collect(Collectors.toCollection(TreeSet::new));
    }

    TreeSet<Screening> findScreeningsByDateAndTime(LocalDate date, LocalTime time) {
        return filterScreenings(screening -> screening.getDate().isEqual(date) &&
                screening.getTime().equals(time));
    }

    TreeSet<Screening> findScreeningsByProjection(Projection projection) {
        return filterScreenings(screening -> screening.getProjection().equals(projection));
    }

    void printProgrammeForToday() {
        printProgramme(filterScreenings(screening -> screening.getDate().isEqual(LocalDate.now())));
    }

    void printProgrammeForDate(LocalDate date) {
        printProgramme(filterScreenings(screening -> screening.getDate().isEqual(date)));
    }

    void printProgrammeForNextWeek() {
        printProgramme(filterScreenings(screening -> {
            LocalDate date = screening.getDate();
            LocalDate now = LocalDate.now();
            return date.isAfter(now.minusDays(1)) && date.isBefore(now.plusDays(7));
        }));
    }

    private void printProgramme(Collection<Screening> screeningCollection) {
        printProgramme(screeningCollection,
                Screening::getDate,
                Screening::getScreeningInfo,
                name);
    }

    static <T> void printProgramme(Collection<T> set, Function<T, LocalDate> dateExtractor,
                                   Function<T, String> lineAppender, String header) {
        StringBuilder sb = new StringBuilder(header);
        LocalDate date = null;
        for (T screening : set) {
            LocalDate apply = dateExtractor.apply(screening);
            if (!apply.equals(date)) {
                date = apply;
                sb.append("\n").append(date).append("\n");
            }
            sb.append(lineAppender.apply(screening));
        }
        out.println(sb);
    }
}
