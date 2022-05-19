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
public class SpotConsumer extends Thread{
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

    public synchronized Queue<Order> getDeliveredOrders() {
        return deliveredOrders;
    }

    public synchronized void printDeliveredOrdes(){
        System.out.println("printing SPOT orders");
        deliveredOrders.forEach(System.out::println);
    }

    public synchronized double calcDeliveryRevenue(){
        Optional<Double> revenue =  deliveredOrders.stream().flatMap(x -> x.getOrderedProduct().stream()).map(MenuEntry::getPrice).reduce(Double::sum);
        return revenue.orElse(0.0D);
    }

    private void deliverOnSpot(){
        System.out.println("starting waiter spot service");
        Optional<Order> order = orderConsumer.getNextSpot();
        Optional<Supplier> supplier = employeeService.takeNextWaiterSupplier();

        if (order.isPresent() && supplier.isPresent()){
            System.out.println("SERVING ORDER ON SPOT");
            supplier.get().setLocked(true);
            try {
                Thread.sleep(TimeMillisecondsConstant.TEN_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("SPOT ORDER DELIVERED BY WAITER");
            Supplier sup = supplier.get();
            deliveredOrders.add(order.get());
            //add function that calc tip to add
            sup.setTip(sup.getTip() + calcTip(order.get()));
            sup.setLocked(false);
        }
    }

    public void killThread(){
        running = false;
        System.out.println("turned spot consumer off");
    }

    private synchronized double calcTip(Order order){
        //check if its lesser then 15 minutes
        Duration durationBetween = Duration.between(order.getOrderTime(), Instant.now());
        double summedOrder = order.getOrderedProduct().stream().map(MenuEntry::getPrice).reduce(Double::sum).orElse(0D);
        if(durationBetween.toMinutes() < 15) {
            double tip = (15 -durationBetween.toMinutes()) * (summedOrder * 0.05);
            return Math.min(tip, summedOrder * 0.1);
        }
        else return 0;
    }

    public boolean canBeRun(){
        return running;
    }
}
