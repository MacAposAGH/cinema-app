import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class CinemaChain {
    private String name;
    private Collection<Cinema> cinemas;

}
