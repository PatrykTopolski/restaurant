package model.employee;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString(callSuper = true)
public class Supplier extends Employee{
    private double tip;
    private Type type;
    private int completedOrders;

    @Builder(builderMethodName = "EmployeeBuilder")
    public Supplier(int id, String name, String surname, String phone, double tip, Type type) {
        super(id, name, surname, phone);
        this.tip = tip;
        this.type = type;
    }

    public enum Type{
        DELIVERY_MAN,
        WAITER,
    }
}
