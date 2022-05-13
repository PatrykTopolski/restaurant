package utils;

import model.Order;
import model.SpotOrder;
import java.util.Comparator;

public class OrdersComparator implements Comparator<Order> {

    @Override
    public int compare(Order a, Order b) {
            if (a instanceof SpotOrder && b instanceof SpotOrder){
                return Integer.compare( a.getId(),  b.getId());
            }else if (a instanceof SpotOrder){
                return -1;
            } else if (b instanceof SpotOrder){
                return 1;
            }
            return Integer.compare(a.getId(), b.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
