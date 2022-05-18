package consumer;

import lombok.RequiredArgsConstructor;
import model.order.Order;
import service.EmployeeService;
import service.OrderService;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
public class OrderConsumer extends Thread{
    private final OrderService orderService;
    private final List<Order> completedOrders;
    private final EmployeeService employeeService;
    private Order bench;
    private boolean isRunning = true;
    private Random random = new Random();
    @Override
    public void run() {
        while (isRunning){
            loadOrderToBench();
            if (bench!= null)
                prepareOrder();
        }
    }

    public synchronized List<Order> getCompletedOrders(){
        return completedOrders;
    }

    public void turnOff(){
        isRunning = false;
        System.out.println("turned kitchen off");
    }

    public boolean canBeRun(){
        return isRunning;
    }

    private void prepareOrder() {
        int THIRTY_SECONDS =  30000;
        System.out.println("taking order from bench");
        bench.getOrderedProduct().forEach(x -> {
            try {
                System.out.println("producing entry from order");
                Thread.sleep(THIRTY_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("finished cooing order ");
        completedOrders.add(bench);
        System.out.println("added order to finished");
    }


    private void loadOrderToBench(){
        Optional<Order> overdueOrder = orderService.getNextDelayedOrder();
        if (overdueOrder.isPresent()){
            System.out.println("received delayed order");
            boolean accepted = random.nextBoolean();
            if (accepted){
                System.out.println("order accepted");
                Order order = overdueOrder.get();
                applyDiscount(order);
                bench = order;
            }else{
                System.out.println("rejected delayed");
                loadOrderToBench();
            }

        }else {
            Optional<Order> nextOrder = orderService.getNextOrder();
            bench = nextOrder.orElse(null);
        }
    }

    private void applyDiscount(Order order){
        order.getOrderedProduct().forEach(product -> product.setPrice(product.getPrice() * 0.8));
    }
}
