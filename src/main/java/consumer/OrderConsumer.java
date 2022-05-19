package consumer;

import lombok.RequiredArgsConstructor;
import model.employee.Cook;
import model.order.DeliveryOrder;
import model.order.Order;
import model.order.SpotOrder;
import service.EmployeeService;
import service.OrderService;
import utils.TimeMillisecondsConstant;

import java.util.Optional;
import java.util.Queue;
import java.util.Random;

@RequiredArgsConstructor
public class OrderConsumer extends Thread{
    private final OrderService orderService;
    private final Queue<Order> completedOrders;
    private final EmployeeService employeeService;
    private Order bench;
    //volatile forces atomic operations (kind of like synch)
    private volatile boolean isRunning = true;
    private Random random = new Random();

    @Override
    public void run() {
        while (isRunning ){
            loadOrderToBench();
            if (bench!= null)
                prepareOrder();
        }
    }

    public synchronized Queue<Order> getCompletedOrders(){
        return completedOrders;
    }

    public void killThread(){
        isRunning = false;
        System.out.println("turned kitchen off");
    }

    public boolean canBeRun(){
        return isRunning;
    }

    public Optional<Order> getNextDeliver(){
        synchronized (completedOrders){
            Optional<Order> order = completedOrders.stream().filter(x -> x instanceof DeliveryOrder).findFirst();
            if (!order.isPresent()){
                try {
                    System.out.println("no next order to DELIVER");
                    completedOrders.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
                completedOrders.remove(order.get());
            return order;
        }
    }

    public Optional<Order> getNextSpot(){
        synchronized (completedOrders){
            Optional<Order> order = completedOrders.stream().filter(x -> x instanceof SpotOrder).findFirst();
            if (!order.isPresent()){
                try {
                    System.out.println("no next order to DELIVER on spot");
                    completedOrders.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // handle the interrupt
                    return Optional.empty();
                }
            }
            else
                completedOrders.remove(order.get());
            return order;
        }
    }

    private void prepareOrder() {
        int milisecounds = TimeMillisecondsConstant.THIRTY_SECONDS;
        for (Cook cook: employeeService.getCooks()
             ) {
            milisecounds-= cook.getCookingTimeReduction();
        }
        System.out.println("time reduced in milis: " + milisecounds);
        int finalTime = milisecounds;
        System.out.println("taking order from bench");
        bench.getOrderedProduct().forEach(x -> {
            try {
                System.out.println("producing entry from order");
                Thread.sleep(finalTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("interrupting");
            }
        });
        System.out.println("finished cooing order ");
        synchronized (completedOrders) {
            completedOrders.add(bench);
            completedOrders.notifyAll();
        }
        System.out.println("added order to finished and notified suppliers");
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
            Optional<Order> nextOrder;
            nextOrder = orderService.getNextOrder();
            bench = nextOrder.orElse(null);
        }
    }

    private void applyDiscount(Order order){
        order.getOrderedProduct().forEach(product -> product.setPrice(product.getPrice() * 0.8));
    }

    public void wakeToKill(){
        synchronized (completedOrders){
            completedOrders.notifyAll();
        }
    }
}
