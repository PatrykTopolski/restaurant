package service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.employee.Cook;
import model.employee.Supplier;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class EmployeeService {
    private final List<Cook> cooks;
    private final List<Supplier> suppliers;

    public void addCook(Cook cook){
        System.out.println("Added new cook");
        cooks.add(cook);
    }

    private void deleteSupplierById(int id){
        Optional<Supplier> supplier = suppliers.stream().filter(x-> x.getId() == id).findFirst();
        if (supplier.isPresent()){
            suppliers.remove(supplier.get());
        }else System.out.println("no such employee");
    }

    private void deleteCookById(int id){
        Optional<Cook> cook = cooks.stream().filter(x-> x.getId() == id).findFirst();
        if (cook.isPresent()){
            cooks.remove(cook.get());
        }else System.out.println("no such employee");
    }
}
