package model.order;

import lombok.*;
import model.menu.MenuEntry;

import java.time.Instant;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString(callSuper = true)
public class SpotOrder extends Order {
    //defined builder for super class on constructor
    @Builder(builderMethodName = "OrderBuilder")
    public SpotOrder(int id, Instant orderTime, List<MenuEntry> OrderedProduct, int tableNumber) {
        super(id, orderTime, OrderedProduct);
        this.tableNumber = tableNumber;
    }

    private int tableNumber;
}
