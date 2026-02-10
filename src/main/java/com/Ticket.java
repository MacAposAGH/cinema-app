package com;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {
    private Customer customer;
    private Screening screening;
    private Seat seat;
}
