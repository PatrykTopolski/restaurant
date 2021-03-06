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
            this.entries.clear();
            entries.addAll(repo.readALl());
        } catch (IOException e) {
            System.out.println("menu file not found\nsave to file first");
        }

    }

    public void saveMenu(){
        try {
            repo.saveAll(this.entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMenuEntry(MenuEntry entry){
        entry.setId(entries.size());
        entries.add(entry);
    }

    public void deleteEntry(int id){
        Optional<MenuEntry> entry =  findById(id) ;
        if (entry.isPresent()){
            entries.remove(entry.get());
        }else{
            System.out.println("invalid Menu ID");
        }
    }

    public void changeAvailability(int id){
        Optional<MenuEntry> entryOptional =  findById(id);
        if (entryOptional.isPresent()){
            MenuEntry entry = entryOptional.get();
            entry.setAvailable(!entry.isAvailable());
        }else{
            System.out.println("invalid Menu ID");
        }
    }

    public void printMenu(){
        entries.stream()
                .sorted(Comparator.comparing(MenuEntry::getId))
                .forEach(System.out::println);
    }

    private List<MenuEntry> generateMenu() {
        List<MenuEntry> generatedList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            generatedList.add(MenuEntry.builder()
                    .id(i)
                    .available(true)
                    .description("description: " + i)
                    .name("menu item : " + i)
                    .price(Math.random() * 50 +1)
                    .build());
        }
        return generatedList;
    }

    public Optional<MenuEntry> findById(int id){
        return entries.stream().filter(x -> x.getId() == id).findFirst();
    }

}
