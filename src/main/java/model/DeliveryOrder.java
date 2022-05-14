package model;

import lombok.*;

import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString(callSuper = true)
public class DeliveryOrder extends Order{
    //defined builder for super class on constructor
    @Builder(builderMethodName = "OrderBuilder")
    public DeliveryOrder(int id, Instant orderTime, List<MenuEntry> OrderedProduct, String deliveryAddress) {
        super(id, orderTime, OrderedProduct);
        this.deliveryAddress = deliveryAddress;
    }

    private String deliveryAddress;
}
