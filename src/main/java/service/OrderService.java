package service;

import lombok.RequiredArgsConstructor;
import model.Order;
import java.util.Optional;
import java.util.PriorityQueue;

@RequiredArgsConstructor
public class OrderService {

    private final PriorityQueue<Order> orderQueue;

    public void addOrder(Order order){
        orderQueue.add(order);
    }

    public Optional<Order> getNextOrder(){
        return Optional.ofNullable(orderQueue.poll());
    }

}
