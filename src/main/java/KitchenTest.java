import model.order.DeliveryOrder;
import model.menu.MenuEntry;
import model.order.Order;

import model.order.SpotOrder;
import consumer.OrderConsumer;
import service.OrderService;
import utils.OrdersComparator;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class KitchenTest {
    public static void main(String[] args) throws InterruptedException {
        PriorityQueue<Order> q = new PriorityQueue<>(new OrdersComparator());
        OrderService service = new OrderService(q);
        addOrdersToService(service);
        OrderConsumer  consumer = new OrderConsumer(service, new ArrayList<>());
        consumer.start();
        Thread.sleep(5000);
        addOrdersToService(service);
        consumer.turnOff();
        System.out.println("ADDED NEW ORDERS");
        System.out.println("notified");
        Thread.sleep(5000);

    }

    private static void addOrdersToService(OrderService service){
        List<MenuEntry> orderedProducts = new ArrayList<>();
        orderedProducts.add(MenuEntry.builder()
                .price(2000)
                .name("test entry")
                .description("for testing").build());

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("testowa 2/1")
                .orderTime(Instant.now().minusMillis(900001))
                .id(0)
                .OrderedProduct(orderedProducts)
                .build());

        orderedProducts.add(MenuEntry.builder().price(2000).name("test entry").description("for testing").build());

        service.addOrder(SpotOrder.OrderBuilder()
                .tableNumber(2)
                .orderTime(Instant.now())
                .id(1)
                .OrderedProduct(orderedProducts)
                .build());

        service.addOrder(SpotOrder.OrderBuilder()
                .tableNumber(1)
                .orderTime(Instant.now())
                .id(4)
                .OrderedProduct(orderedProducts)
                .build());

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("ulica 1/2")
                .orderTime(Instant.now())
                .id(2)
                .OrderedProduct(orderedProducts)
                .build());
    }
}
