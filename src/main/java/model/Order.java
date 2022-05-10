package model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Order {
    private int id;
    private LocalDate orderTime;
    private List<MenuEntry> OrderedProduct;
}
