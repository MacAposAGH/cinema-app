package com;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class Room {
    private String name;
    private  Collection<Seat> seats ;

    protected void addSeat(Seat seat){
        seats.add(seat);
    }

    protected void removeSeat(Seat seat){
        seats.remove(seat);
    }
}
