package model.order;

import lombok.Builder;
import lombok.Data;
import model.menu.MenuEntry;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class Order {
    private int id;
    private Instant orderTime;
    private List<MenuEntry> OrderedProduct;
}
