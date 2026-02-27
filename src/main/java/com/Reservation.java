package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reservation {
    private Screening screening;
    private Seat seat;
    private boolean isPrepaid;
    private Customer customer;
}
