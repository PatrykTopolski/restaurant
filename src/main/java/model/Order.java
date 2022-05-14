package model;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class Order {
    private int id;
    private Instant orderTime;
    private List<MenuEntry> OrderedProduct;
}
