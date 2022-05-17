package consumer;

import lombok.RequiredArgsConstructor;
import model.order.Order;
import service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderConsumer extends Thread{
    private final OrderService orderService;
    private final List<Order> completedOrders;
    private Order bench;
    private boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning){
            loadOrderToBench();
            if (bench!= null)
                prepareOrder();
        }
    }

    public void turnOff(){
        isRunning = false;
        System.out.println("turned kitchen off");
    }

    private void prepareOrder() {
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


    private void loadOrderToBench(){
        Optional<Order> overdueOrder = orderService.getNextDelayedOrder();
        if (overdueOrder.isPresent()){
            bench = overdueOrder.get();
        }else {
            Optional<Order> nextOrder = orderService.getNextOrder();
            bench = nextOrder.orElse(null);
        }
    }
}
