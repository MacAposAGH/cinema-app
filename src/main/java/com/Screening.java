package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Screening {
    private LocalDateTime time;
    private LocalDate date;
    private Movie movie;
    private Room room;
}
