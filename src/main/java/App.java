import controller.ApplicationController;
import model.order.DeliveryOrder;
import model.menu.MenuEntry;
import model.order.SpotOrder;
import repository.MenuRepositoryImpl;
import repository.Repository;
import service.MenuService;


import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        List<MenuEntry> entry = new ArrayList<>();
        entry.add(MenuEntry.builder()
                .name("lazania")
                .available(true)
                .description("mmmmm")
                .price(19.99)
                .build());
        entry.add(MenuEntry.builder()
                .name("spagetti bolonieze")
                .available(true)
                .description("codzienna dieta")
                .price(27.99)
                .build());
        entry.add(MenuEntry.builder()
                .name("tatar")
                .available(true)
                .description("99% miensa")
                .price(5.99)
                .build());
        ApplicationController appController = new ApplicationController(
                new Scanner(System.in),
                new MenuService(new MenuRepositoryImpl(), entry));
        appController.runApplication();

    }
}
