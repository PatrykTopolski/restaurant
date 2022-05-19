package service;

import exceptions.LastEmployeeException;
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
    private final List<Supplier> deliverySuppliers;
    private final List<Supplier> waiterSuppliers;
    private volatile int suppliersNumber = 0;
    private volatile int cooksNumber = 0;


    public synchronized void addSupplier(Supplier supplier){
        supplier.setId(suppliersNumber++);
        System.out.println("Added new supplier");
        if (supplier.getType().equals(Supplier.Type.DELIVERY_MAN)){
            synchronized (deliverySuppliers) {
                deliverySuppliers.add(supplier);
                deliverySuppliers.notify();
            }

        }else {
            synchronized (waiterSuppliers) {
                waiterSuppliers.add(supplier);
                waiterSuppliers.notify();
            }
        }
    }

    public synchronized void addCook(Cook cook){
        cook.setId(cooksNumber++);
        System.out.println("Added new cook");
        cooks.add(cook);
    }


    public Optional<Supplier> takeNextDeliverySupplier(){
        synchronized (deliverySuppliers) {
            Optional<Supplier> sup = deliverySuppliers.stream()
                    .filter(x-> !x.isLocked())
                    .findFirst();
            if (!sup.isPresent()) {
                try {
                    System.out.println("all delivery suppliers are busy");
                    deliverySuppliers.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sup;
        }
    }

    public Optional<Supplier> takeNextWaiterSupplier(){
        synchronized (waiterSuppliers) {
            Optional<Supplier> sup = waiterSuppliers.stream()
                    .filter(x-> !x.isLocked())
                    .findFirst();
            if (!sup.isPresent()) {
                try {
                    System.out.println("all suppliers are busy");
                    waiterSuppliers.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sup;
        }
    }

    public synchronized void deleteSupplierById(int id){
        Optional<Supplier> supplier = deliverySuppliers.stream().filter(x-> x.getId() == id).findFirst();
        if (supplier.isPresent()){
            deliverySuppliers.remove(supplier.get());
            System.out.println("supplier deleted");
            if (deliverySuppliers.size()==0){
                throw new LastEmployeeException();
            }
        }else {
            Optional<Supplier> waiterSupp = waiterSuppliers.stream().filter(x-> x.getId() == id).findFirst();
            if (waiterSupp.isPresent()){
                waiterSuppliers.remove(waiterSupp.get());
                System.out.println("waiter deleted");
                if (waiterSuppliers.size()==0){
                    throw new LastEmployeeException();
                }
            }else
                System.out.println("no such supplier");
        }
    }

    public synchronized void deleteCookById(int id){

        Optional<Cook> cook = cooks.stream().filter(x-> x.getId() == id).findFirst();
        if (cook.isPresent()){
            cooks.remove(cook.get());
            if (cooks.size() == 0){
                throw new LastEmployeeException();
            }
        }else System.out.println("no such employee");
    }

    public synchronized void printEmployees(){
        System.out.println("cooks");
        cooks.forEach(System.out::println);
        System.out.println("waiter suppliers");
        waiterSuppliers.forEach(System.out::println);
        System.out.println("deliveryman suppliers");
        deliverySuppliers.forEach(System.out::println);

    }

    public void wakeToKill(){
        synchronized (deliverySuppliers){
            deliverySuppliers.notifyAll();
        }
        synchronized (waiterSuppliers){
            waiterSuppliers.notifyAll();
        }
    }
}
