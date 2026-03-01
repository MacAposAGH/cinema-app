package com;

import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;
import java.util.function.Function;

public class CinemaUtil {
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
        System.out.println(sb);
    }
}
