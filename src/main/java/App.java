import consumer.DeliveryConsumer;
import consumer.OrderConsumer;
import consumer.SpotConsumer;
import controller.ApplicationController;
import model.employee.Cook;
import model.employee.Supplier;
import model.order.DeliveryOrder;
import model.menu.MenuEntry;
import model.order.Order;
import model.order.SpotOrder;
import repository.MenuRepositoryImpl;
import service.EmployeeService;
import service.MenuService;
import service.OrderService;
import utils.MenuEntryUtil;
import utils.OrdersComparator;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class App {
    public static void main(String[] args){
        //here spring would do most of the work injecting components where needed (autowired or final class fields), instead I do it manually
        PriorityQueue<Order> q = new PriorityQueue<>(new OrdersComparator());
        List<MenuEntry> entry = new ArrayList<>();
        EmployeeService employeeService = new EmployeeService(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        OrderService orderService = new OrderService(q);
        OrderConsumer orderConsumer = new OrderConsumer(orderService, new LinkedList<>(), employeeService );
        addOrdersToService(orderService);
        addOrdersToService(orderService);
        initEntries(entry);
        populateEmployees(employeeService);
        ApplicationController appController = new ApplicationController(
                        new Scanner(System.in),
                        new MenuService(new MenuRepositoryImpl(), entry),
                        orderService,
                employeeService,
                orderConsumer,
                new DeliveryConsumer(orderConsumer,employeeService,new LinkedList<>()),
                new SpotConsumer(orderConsumer,employeeService,new LinkedList<>()));
        appController.runApplication();

    }

    private static void populateEmployees(EmployeeService employeeService){
        for (int i = 0; i < 4; i++) {
           employeeService.addCook(Cook.EmployeeBuilder()
                   .cookingTimeReduction(7000)
                   .id(i)
                   .name("test name " +i)
                   .phone("testPhone")
                   .surname("test surname " + i)
                   .build());

        }
        employeeService.addSupplier(Supplier.EmployeeBuilder()
                .type(Supplier.Type.DELIVERY_MAN)
                .name("Patryk")
                .phone("test")
                .surname("test")
                .tip(0)
                .build());
        employeeService.addSupplier(Supplier.EmployeeBuilder()
                .type(Supplier.Type.WAITER)
                .name("Patryk")
                .phone("test")
                .surname("test")
                .tip(0)
                .build());
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
        entry.add(MenuEntry.builder()
                .id(2)
                .name("chili con carne")
                .available(true)
                .description("Papica!")
                .price(30.99)
                .build());
        entry.add(MenuEntry.builder()
                .id(2)
                .name("burgerson")
                .available(true)
                .description("with cheese")
                .price(45.99)
                .build());
    }

    private static void addOrdersToService(OrderService service){
        List<MenuEntry> orderedProducts = new ArrayList<>();
        MenuEntry testEntry = MenuEntry.builder()
                .price(2000)
                .name("test entry")
                .description("for testing").build();

        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("testowa 2/1")
                .orderTime(Instant.now().minusMillis(900001))
                .id(0)
                .OrderedProduct(orderedProducts)
                .build());

        orderedProducts = new ArrayList<>();
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));

        service.addOrder(SpotOrder.OrderBuilder()
                .tableNumber(2)
                .orderTime(Instant.now())
                .id(1)
                .OrderedProduct(orderedProducts)
                .build());

        orderedProducts = new ArrayList<>();
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));

        service.addOrder(SpotOrder.OrderBuilder()
                .tableNumber(1)
                .orderTime(Instant.now())
                .id(4)
                .OrderedProduct(orderedProducts)
                .build());

        orderedProducts = new ArrayList<>();
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("ulica 1/2")
                .orderTime(Instant.now())
                .id(2)
                .OrderedProduct(orderedProducts)
                .build());

        orderedProducts = new ArrayList<>();
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));
        orderedProducts.add(MenuEntryUtil.cloneMenuEntry(testEntry));

        service.addOrder(DeliveryOrder.OrderBuilder()
                .deliveryAddress("ulica 1/2")
                .orderTime(Instant.now().minusMillis(900001))
                .id(2)
                .OrderedProduct(orderedProducts)
                .build());
    }


}
