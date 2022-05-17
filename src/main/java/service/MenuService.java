package service;


import lombok.RequiredArgsConstructor;
import model.menu.MenuEntry;
import repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MenuService {
    private final Repository<MenuEntry> repo;
    private final List<MenuEntry> entries;


    public void loadMenuFromFile() {
        System.out.println("loading menu");
        try {
            entries.addAll(repo.readALl());
        } catch (IOException e) {
            System.out.println("menu file not found");
//            this.entries = generateMenu();
        }
    }

    public void saveMenu(){
        try {
            repo.saveAll(this.entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfEntries(){
        return entries.size();
    }
    public void addMenuEntry(MenuEntry entry){
        entries.add(entry);
    }

    public void deleteEntry(int id){
        entries.remove(id);
    }

    public void changeAvailability(int id){
       entries.get(id).setAvailable(!entries.get(id).isAvailable());
    }

    public void printMenu(){
        for (int i = 0; i < entries.size(); i++) {
            System.out.println(i + ". " + entries.get(i).toString());
        }
    }

    private List<MenuEntry> generateMenu() {
        List<MenuEntry> generatedList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            generatedList.add(MenuEntry.builder()
                    .available(true)
                    .description("description: " + i)
                    .name("menu item : " + i)
                            .price(Math.random() * 50 +1)
                    .build());
        }
        return generatedList;
    }
}
