import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

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
