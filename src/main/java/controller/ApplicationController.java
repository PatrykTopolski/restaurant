package controller;

import lombok.RequiredArgsConstructor;
import model.menu.MenuEntry;
import service.MenuService;

import java.io.BufferedReader;
import java.util.InputMismatchException;
import java.util.Scanner;

@RequiredArgsConstructor
public class ApplicationController {
    private final Scanner scanner;
    private final MenuService menuService;

    public void runApplication(){
        boolean running = true;
        while (running){
            int input = getInputMainMenu();
            switch(input) {
                case 1:
                    menuService.printMenu();
                    break;
                case 2:
                    // code block
                    menuService.addMenuEntry(MenuEntry.builder()
                            .available(true)
                            .name(getInputString("provide entry name"))
                            .price(getInputDouble("provide price"))
                            .description(getInputString("provide description"))
                            .build());
                    break;
                case 3:
                    // code block
                    break;
                case 15:
                    running = false;
                    System.out.println("closing app");
                    break;
                default:
                    System.out.println("wrong input");
                    // code block
            }
        }
    }

    private int getInputMainMenu(){
        System.out.println("1. show menu\n"
                + "2. add entry to menu\n"
                + "3. set menu entry as unavailable\n"
                + "4. load menu from file\n"
                + "5. create order\n"
                + "6. create random order\n"
                + "7. show orders in queue\n"
                + "8. show completed orders\n"
                + "9. calculate revenue\n"
                + "10. add new employee\n"
                + "11. delete employee\n"
                + "12. execute order 66\n"
                + "13. stop executing orders\n"
                + "14. show employee details\n"
                + "15. exit");
        return scanner.nextInt();
    }

    private String getInputString(String askForInput){
        System.out.println(askForInput);
        return scanner.next();
    }

    private int getInputInt(String askForInput){
        System.out.println(askForInput);
        return scanner.nextInt();
    }

    private double getInputDouble(String askForInput){
        //aware taking advantage of unboxing
        Double d = null;
        while (d == null){
            System.out.println(askForInput);
            try {
                d =  scanner.nextDouble();
            }catch (InputMismatchException e){
                System.out.println("wrong input, try comma instead of dot");
                scanner.nextLine();
            }
        }
        return d;
    }


}
