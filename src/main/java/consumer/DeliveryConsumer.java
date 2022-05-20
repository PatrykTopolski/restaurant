package consumer;


import lombok.RequiredArgsConstructor;
import model.employee.Supplier;
import model.menu.MenuEntry;
import model.order.Order;
import service.EmployeeService;
import utils.TimeMillisecondsConstant;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Queue;

@RequiredArgsConstructor
public class DeliveryConsumer extends Thread{
    private final OrderConsumer orderConsumer;
    private final EmployeeService employeeService;
    private final Queue<Order> deliveredOrders;
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running){
            deliverOnSpot();
        }
    }

    public Queue<Order> getDeliveredOrders() {
        return deliveredOrders;
    }

    public synchronized void printDeliveredOrdes(){
        System.out.println("printing DELIVERED orders");
        deliveredOrders.forEach(System.out::println);
    }

    public synchronized double calcDeliveryRevenue(){
        Optional<Double> revenue =  deliveredOrders.stream().flatMap(x -> x.getOrderedProduct().stream()).map(MenuEntry::getPrice).reduce(Double::sum);
        return revenue.orElse(0.0D);
    }

    private void deliverOnSpot(){
        /*
        here it works only for 1 supplier at a given time
        i should get all suppliers that are not locked and for each
        load order waiting for delivery or even do it with threadPool
        lack of time to implement
         */
        System.out.println("starting delivery service");
        Optional<Order> order = orderConsumer.getNextDeliver();
        Optional<Supplier> supplier = employeeService.takeNextDeliverySupplier();
        if (order.isPresent() && supplier.isPresent()){
            System.out.println("DELIVERING ORDER");
            supplier.get().setLocked(true);
            try {
                Thread.sleep(TimeMillisecondsConstant.TWO_MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ORDER DELIVERED TO CLIENT");
            Supplier sup = supplier.get();
            deliveredOrders.add(order.get());
            //add function that calc tip to add
            sup.setTip(sup.getTip() + calcTip(order.get()));
            sup.setLocked(false);
        }
    }

    private synchronized double calcTip(Order order){
        Duration durationBetween = Duration.between(order.getOrderTime(), Instant.now());
        double summedOrder = order.getOrderedProduct().stream().map(MenuEntry::getPrice).reduce(Double::sum).orElse(0D);
        double tip =  durationBetween.toMinutes() * (summedOrder *0.05);
        return Math.min(tip, summedOrder * 0.1);

    }

    public boolean canBeRun(){
        return running;
    }

    public void killThread(){
        running = false;
        System.out.println("turned delivery consumer off");
    }

}
