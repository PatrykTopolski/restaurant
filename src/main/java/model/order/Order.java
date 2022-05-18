package model.order;

import lombok.Builder;
import lombok.Data;
import model.menu.MenuEntry;

import java.time.Instant;
import java.util.List;


/*
could be abstract class since order have to be either on spot
or on delivery, but I wanted to implement builder including this class fields
using lombok annotations
 */
@Data
@Builder
public class Order {
    private int id;
    private Instant orderTime;
    private List<MenuEntry> OrderedProduct;
}
