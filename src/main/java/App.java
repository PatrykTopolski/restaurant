import model.order.DeliveryOrder;
import model.menu.MenuEntry;
import model.order.SpotOrder;
import repository.MenuRepositoryImpl;
import repository.Repository;


import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        List<MenuEntry> entry = new ArrayList<>();
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

        Repository<MenuEntry> repo = new MenuRepositoryImpl();
        repo.saveAll(entry);
        repo.readALl().forEach(System.out::println);
        DeliveryOrder deliveryOrder = DeliveryOrder
                .OrderBuilder()
                .id(1)
                .orderTime(Instant.now())
                .OrderedProduct(null)
                .deliveryAddress("test address")
                .build();
        SpotOrder onSpotOrder = SpotOrder
                .OrderBuilder()
                .id(1)
                .orderTime(Instant.now())
                .OrderedProduct(null)
                .tableNumber(1)
                .build();
        System.out.println(deliveryOrder);
        System.out.println(onSpotOrder);
    }
}
