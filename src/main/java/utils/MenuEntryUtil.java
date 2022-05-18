package utils;

import model.menu.MenuEntry;

public class MenuEntryUtil {
    public static MenuEntry cloneMenuEntry(MenuEntry menuEntry){
        return MenuEntry.builder()
                .id(menuEntry.getId())
                .description(menuEntry.getDescription())
                .name(menuEntry.getName())
                .price(menuEntry.getPrice())
                .available(menuEntry.isAvailable())
                .build();
    }
}
