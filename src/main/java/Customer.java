import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Data
@AllArgsConstructor
public class Customer {
    private String name;
    private String surname;
    private String email;
}
