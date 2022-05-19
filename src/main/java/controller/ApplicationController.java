package controller;

import consumer.DeliveryConsumer;
import consumer.OrderConsumer;
import consumer.SpotConsumer;
import exceptions.LastEmployeeException;
import lombok.AllArgsConstructor;
import model.employee.Cook;
import model.employee.Employee;
import model.employee.Supplier;
import model.menu.MenuEntry;
import model.order.DeliveryOrder;
import model.order.Order;
import model.order.SpotOrder;
import service.EmployeeService;
import service.MenuService;
import service.OrderService;
import utils.MenuEntryUtil;


import java.time.Instant;
import java.util.*;

@AllArgsConstructor
public class ApplicationController {
    private final Scanner scanner;
    private final MenuService menuService;
    private final OrderService orderService;
    private final EmployeeService employeeService;
    private OrderConsumer orderConsumer;
    private DeliveryConsumer deliveryConsumer;
    private SpotConsumer spotConsumer;

    public void runApplication(){
        boolean running = true;
        while (running){
            int input = getInputMainMenu();
            switch(input) {
                case 1:
                    menuService.printMenu();
                    break;
                case 2:
                    menuService.addMenuEntry(MenuEntry.builder()
                            .available(true)
                            .name(getInputString("provide entry name"))
                            .price(getInputDouble("provide price"))
                            .description(getInputString("provide description"))
                            .build());

                    break;
                case 3:
                    menuService.changeAvailability(getInputInt("provide menu entry id:"));
                    break;
                case 4:
                    menuService.loadMenuFromFile();
                    break;
                case 5:
                    menuService.saveMenu();
                    break;
                case 6:
                    int orderType = getInputInt("1. create order on spot\n2.create order on delivery");
                    if (orderType == 1){
                        orderService.addOrder(SpotOrder.OrderBuilder()
                                .tableNumber(getInputInt("provide table number"))
                                .orderTime(Instant.now())
                                .OrderedProduct(getOrderEntryList())
                                .build());
                    }else if (orderType == 2){
                        orderService.addOrder(DeliveryOrder.OrderBuilder()
                                .deliveryAddress(getInputString("provide address: "))
                                .orderTime(Instant.now())
                                .OrderedProduct(getOrderEntryList())
                                .build());
                    }else System.out.println("wrong input");
                    break;
                case 7:
                    break;
                case 8:
                    orderService.printOrdersInQueue();
                    break;
                case 9:
                    /*
                    to print in the correct order I would add field "time completed"
                    then combine both Queues then Compare by date and print
                    but I don't have time to implement
                    */
                    spotConsumer.printDeliveredOrdes();
                    deliveryConsumer.printDeliveredOrdes();
                    break;
                case 10:
                    double spotRevenue = spotConsumer.calcDeliveryRevenue();
                    double deliveryRevenue = deliveryConsumer.calcDeliveryRevenue();
                    System.out.println("spot revenue: " + spotRevenue);
                    System.out.println("delivery revenue: " + deliveryRevenue);
                    double sum = spotRevenue + deliveryRevenue;
                    System.out.println("global revenue: " + sum);
                    break;
                case 11:
                    int employeeType = getInputInt("1. add new cook\n2.add new delivery man\n3. add new waiter");
                    if (employeeType == 1){
                        employeeService.addCook(Cook.EmployeeBuilder()
                                .cookingTimeReduction(getInputInt("provide time reduction in milliseconds"))
                                .name(getInputString("provide name"))
                                .surname(getInputString("provide surname"))
                                .phone(getInputString("provide phone"))
                                .build());
                    }else if (employeeType == 2) {
                        employeeService.addSupplier(Supplier.EmployeeBuilder()
                                .type(Supplier.Type.DELIVERY_MAN)
                                .tip(0)
                                .name(getInputString("provide name"))
                                .surname(getInputString("provide surname"))
                                .phone(getInputString("provide phone"))
                                .build());
                    } else if (employeeType == 3) {
                        employeeService.addSupplier(Supplier.EmployeeBuilder()
                                .type(Supplier.Type.WAITER)
                                .tip(0)
                                .name(getInputString("provide name"))
                                .surname(getInputString("provide surname"))
                                .phone(getInputString("provide phone"))
                                .build());
                    }
                    break;
                case 12:
                    int employeeTypeDel = getInputInt("1. delete cook\n2. delete supplier");
                    try {
                        if (employeeTypeDel == 1) {
                            employeeService.deleteCookById(getInputInt("provide cook id"));
                        } else employeeService.deleteSupplierById(getInputInt("provide supplier id"));
                    }catch (LastEmployeeException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 13:
                    if (orderConsumer.canBeRun() && spotConsumer.canBeRun() && deliveryConsumer.canBeRun()){
                        System.out.println("starting consumer");
                        orderConsumer.start();
                        deliveryConsumer.start();
                        spotConsumer.start();

                    }else {
                        System.out.println("old consumers cant be run, creating new");
                        Queue<Order> completedTemp = orderConsumer.getCompletedOrders();
                        Queue<Order> deliveredToPlace = spotConsumer.getDeliveredOrders();
                        Queue<Order> deliveredOnSpot = spotConsumer.getDeliveredOrders();
                        orderConsumer = new OrderConsumer(orderService, completedTemp, employeeService);
                        spotConsumer = new SpotConsumer(orderConsumer, employeeService, deliveredOnSpot);
                        deliveryConsumer = new DeliveryConsumer(orderConsumer, employeeService, deliveredToPlace);
                        System.out.println("starting new consumers");
                        orderConsumer.start();
                    }
                    break;
                case 14:
                    System.out.println("turning off consumer");
                    wakeAndKillThreads();
                    break;
                case 15:
                    employeeService.printEmployees();
                    break;
                case 16:
                    wakeAndKillThreads();
                    running = false;
                    break;
                default:
                    System.out.println("wrong input");
                    // code block
            }
        }
    }

    private void wakeAndKillThreads(){
        orderConsumer.wakeToKill();
        employeeService.wakeToKill();
        spotConsumer.killThread();
        deliveryConsumer.killThread();
        orderService.wakeToKill();
        orderConsumer.killThread();
    }


    private List<MenuEntry> getOrderEntryList(){
        List<MenuEntry> orderList = new ArrayList<>();
        boolean addingNextEntry = true;
        while (addingNextEntry){
            menuService.printMenu();
            int orderId =  getInputInt("\nprovide id of entry from menu: ");
            Optional<MenuEntry> entry = menuService.findById(orderId);
            if (entry.isPresent()){
                orderList.add(MenuEntryUtil.cloneMenuEntry(entry.get()));
            }else System.out.println("wrong entry id");
            String askingNext = getInputString("add next entry to order? Y/N");
            if (!askingNext.equals("Y"))
                addingNextEntry = false;
        }
        return orderList;
    }

    private int getInputMainMenu(){
        System.out.println("1. show menu\n"
                + "2. add entry to menu\n"
                + "3. set menu entry as unavailable/available\n"
                + "4. load menu from file, this operation will overwrite current menu\n"
                + "5. save current menu to file\n"
                + "6. create order\n"
                + "7. create random order\n"
                + "8. show orders in queue\n"
                + "9. show completed orders\n"
                + "10. calculate revenue\n"
                + "11. add new employee\n"
                + "12. delete employee\n"
                + "13. execute order 66 (kitchen)\n"
                + "14. stop executing orders(wait until current consumers are done until starting new ones)\n"
                + "15. show employee details\n"
                + "16. exit");
        return scanner.nextInt();
    }

    private String getInputString(String askForInput){
        System.out.println(askForInput);
        return scanner.next();
    }

    private int getInputInt(String askForInput){

        //aware taking advantage of unboxing
        Integer i = null;
        while (i == null){
            System.out.println(askForInput);
            try {
                i =  scanner.nextInt();
            }catch (InputMismatchException e){
                System.out.println("wrong input, try comma instead of dot");
                scanner.nextLine();
            }
        }
        return i;
    }

    private double getInputDouble(String askForInput){
        //aware taking advantage of unboxing
        Double d = null;
        while (d == null){
            System.out.println(askForInput);
            try {
                d =  scanner.nextDouble();
            }catch (InputMismatchException e){
                System.out.println("wrong input, try comma instead of dot");
                scanner.nextLine();
            }
        }
        return d;
    }


}
