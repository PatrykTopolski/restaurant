package model.employee;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString(callSuper = true)
public class Cook extends Employee{
    private int cookingTimeReduction;

    @Builder(builderMethodName = "EmployeeBuilder")
    public Cook(int id, String name, String surname, String phone, int cookingTimeReduction) {
        super(id, name, surname, phone);
        this.cookingTimeReduction = cookingTimeReduction;
    }
}
