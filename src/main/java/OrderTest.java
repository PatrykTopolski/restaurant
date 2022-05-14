import model.DeliveryOrder;
import model.Order;
import model.SpotOrder;
import service.OrderService;
import utils.OrdersComparator;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.PriorityQueue;

public class OrderTest {


    public static void main(String[] args) {
        PriorityQueue<Order> q = new PriorityQueue<>(new OrdersComparator());
        OrderService service = new OrderService(q);

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("testowa 2/1")
                .orderTime(Instant.now().minusMillis(900001))
                .id(0)
                .OrderedProduct(null)
                .build());


        service.addOrder(SpotOrder.OrderBuilder()
                .tableNumber(2)
                .orderTime(Instant.now())
                .id(1)
                .OrderedProduct(null)
                .build());

        service.addOrder(SpotOrder.OrderBuilder()
                .tableNumber(1)
                .orderTime(Instant.now())
                .id(4)
                .OrderedProduct(null)
                .build());

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("ulica 1/2")
                .orderTime(Instant.now())
                .id(2)
                .OrderedProduct(null)
                .build());


        System.out.println("delayed order : " + service.getNextDelayedOrder());

        System.out.println("===========================");
        System.out.println("next order is: " +service.getNextOrder());
        System.out.println("next order is: " +service.getNextOrder());
        System.out.println("next order is: " +service.getNextOrder());
        System.out.println("next order is: " +service.getNextOrder());
        System.out.println("next order is: " +service.getNextOrder());
        System.out.println("end");
    }
}
