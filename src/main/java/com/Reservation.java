package com;

import lombok.AllArgsConstructor;
import lombok.Data;

public record Reservation(Screening screening, Seat seat, boolean isPrepaid, Customer customer) {
}
