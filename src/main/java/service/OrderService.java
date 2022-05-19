package service;

import lombok.RequiredArgsConstructor;
import model.order.Order;
import utils.TimeMillisecondsConstant;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.PriorityQueue;


@RequiredArgsConstructor
public class OrderService {
    private int orderNumber = 0;

    private final PriorityQueue<Order> orderQueue;


    public  void addOrder(Order order){
        synchronized (orderQueue) {
            order.setId(orderNumber++);
            orderQueue.add(order);
            orderQueue.notify();
            System.out.println("notified");
        }
    }

    public synchronized void printOrdersInQueue(){
        orderQueue.forEach(System.out::println);
    }

    public Optional<Order> getNextOrder(){
        synchronized (orderQueue) {
            if(orderQueue.isEmpty()){
                try {
                    System.out.println("waiting for order");
                    orderQueue.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // handle the interrupt
                    return Optional.empty();
                }
            }
            return Optional.ofNullable(orderQueue.poll());
        }
    }

    public synchronized Optional<Order> getNextDelayedOrder(){
        Optional<Order> overdueOrder = orderQueue.stream().filter(order -> {
            Duration durationBetween = Duration.between(order.getOrderTime(), Instant.now());
            return durationBetween.toMillis() > TimeMillisecondsConstant.FIFTEEN_MINUTES;
        }).findFirst();
        overdueOrder.ifPresent(orderQueue::remove);
        return overdueOrder;
    }

    public synchronized void delete(Order order){
        orderQueue.remove(order);
    }


    //should use interrupted to kill thread, but I have just one thread so its easier
    public void wakeToKill(){
        synchronized (orderQueue){
            orderQueue.notifyAll();
        }
    }

}
