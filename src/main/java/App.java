import consumer.OrderConsumer;
import controller.ApplicationController;
import model.employee.Cook;
import model.employee.Supplier;
import model.order.DeliveryOrder;
import model.menu.MenuEntry;
import model.order.Order;
import model.order.SpotOrder;
import repository.MenuRepositoryImpl;
import repository.Repository;
import service.EmployeeService;
import service.MenuService;
import service.OrderService;
import utils.OrdersComparator;


import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class App {
    public static void main(String[] args){
        //here spring would do most of the work injecting components where needed (autowired or final class fields), instead I do it manually
        PriorityQueue<Order> q = new PriorityQueue<>(new OrdersComparator());
        List<MenuEntry> entry = new ArrayList<>();
        EmployeeService employeeService = new EmployeeService(new ArrayList<>(), new ArrayList<>());
        OrderService orderService = new OrderService(q);
        addOrdersToService(orderService);
        initEntries(entry);
        ApplicationController appController = new ApplicationController(
                        new Scanner(System.in),
                        new MenuService(new MenuRepositoryImpl(), entry),
                        orderService,
                employeeService,
                new OrderConsumer(orderService, new LinkedList<>(), employeeService ));
        appController.runApplication();

    }

    private static void initEntries(List<MenuEntry> entry){
        entry.add(MenuEntry.builder()
                .id(0)
                .name("lazania")
                .available(true)
                .description("mmmmm")
                .price(19.99)
                .build());
        entry.add(MenuEntry.builder()
                .id(1)
                .name("spagetti bolonieze")
                .available(true)
                .description("codzienna dieta")
                .price(27.99)
                .build());
        entry.add(MenuEntry.builder()
                .id(2)
                .name("tatar")
                .available(true)
                .description("99% miensa")
                .price(5.99)
                .build());
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
