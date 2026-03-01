package com;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public record Cinema(
        String name,
        String address,
        TreeSet<Room> rooms,
        HashSet<Movie> movies,
        TreeMap<ScreeningKey, Screening> screenings,
        HashSet<Customer> customers) {

    public Cinema {
        screenings.values().forEach(this::checkScreenings);
    }

    public Cinema(String name, String address, TreeSet<Room> rooms, HashSet<Movie> movies) {
        this(name, address, rooms, movies, new TreeMap<>(), new HashSet<>());
    }

    void addScreenings(Screening screening) {
        checkScreenings(screening);
        screenings.put(new ScreeningKey(screening), screening);
    }

    void addCustomer(Customer customer) {
        customers.add(customer);
    }

    private void checkScreenings(Screening screening) {
        if (!movies.contains(screening.getMovie()) || !rooms.contains(screening.getRoom())) {
            throw new IllegalArgumentException("Can't assign one of the screening. " +
                    "No such movie or room in this cinema!");
        }
    }

    private TreeSet<Screening> filterScreenings(Predicate<Screening> predicate) {
        return screenings.values().stream().filter(predicate).collect(Collectors.toCollection(TreeSet::new));
    }

    TreeSet<Screening> findScreeningsForToday() {
        return filterScreenings(screening -> screening.getDate().isEqual(LocalDate.now()));
    }

    TreeSet<Screening> findScreeningsByDate(LocalDate date) {
        return filterScreenings(screening -> screening.getDate().isEqual(date));
    }

    TreeSet<Screening> findScreeningsForNextWeek() {
        LocalDate now = LocalDate.now();
        return filterScreenings(screening -> {
            LocalDate date = screening.getDate();
            return date.isAfter(now.minusDays(1)) && date.isBefore(now.plusDays(7));
        });
    }

    TreeSet<Screening> findScreeningsByProjection(Projection projection) {
        return filterScreenings(screening -> screening.getProjection().equals(projection));
    }

    void updateScreenings(TreeSet<Screening> set, Consumer<Screening> screeningUpdater) {
        for (Screening screening : set) {
            ScreeningKey oldScreeningKey = new ScreeningKey(screening);
            if (screenings.remove(oldScreeningKey) == null) {
                throw new IllegalArgumentException("No such screening in this cinema!");
            }
            screeningUpdater.accept(screening);
            ScreeningKey newScreeningKey = new ScreeningKey(screening);

            Screening existingScreening = screenings.get(newScreeningKey);
            if (existingScreening != null) {
                Screening updateScreening = new Screening(
                        oldScreeningKey.time(), oldScreeningKey.date(), oldScreeningKey.room(),
                        existingScreening.getMovie(), existingScreening.getProjection());
                screenings.put(oldScreeningKey, updateScreening);
            }
            screenings.put(newScreeningKey, screening);
        }
    }

    void printProgramme() {
        CinemaUtil.printProgramme( screenings.values(),
                Screening::getDate, Screening::getScreeningInfo, name);
    }

}
