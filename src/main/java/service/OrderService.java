package service;

import lombok.RequiredArgsConstructor;
import model.order.Order;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.PriorityQueue;

@RequiredArgsConstructor
public class OrderService {

    private final PriorityQueue<Order> orderQueue;

    public synchronized void addOrder(Order order){
        orderQueue.add(order);
    }

    public synchronized Optional<Order> getNextOrder(){
        return Optional.ofNullable(orderQueue.poll());
    }

    public synchronized Optional<Order> getNextDelayedOrder(){
        Optional<Order> overdueOrder = orderQueue.stream().filter(order -> {
            long FIFTEEN_MINUTES = 900000;
            Duration durationBetween = Duration.between(order.getOrderTime(), Instant.now());
            return durationBetween.toMillis() > FIFTEEN_MINUTES;
        }).findFirst();
        overdueOrder.ifPresent(orderQueue::remove);
        return overdueOrder;
    }

    public synchronized void delete(Order order){
        orderQueue.remove(order);
    }

}
