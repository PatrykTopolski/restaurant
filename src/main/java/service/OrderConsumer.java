package service;

import lombok.RequiredArgsConstructor;
import model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderConsumer extends Thread{
    private final OrderService orderService;
    private List<Order> completedOrders = new ArrayList<>();
    private Order bench;


    public void consumeOrder() {
        int THIRTY_SECONDS =  300;
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


    public void loadOrderToBench(){
        Optional<Order> overdueOrder = orderService.getNextDelayedOrder();
        if (overdueOrder.isPresent()){
            bench = overdueOrder.get();
        }else {
            Optional<Order> nextOrder = orderService.getNextOrder();
            bench = nextOrder.orElse(null);
        }
    }

    @Override
    public void run() {
        while (true){
            loadOrderToBench();
            if (bench!= null)
                consumeOrder();
        }
    }
}
