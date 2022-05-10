package repository;

import model.MenuEntry;

import java.io.*;
import java.util.List;
public interface CRUD<T, I>{
    void saveAll(List<T> t) throws IOException;
    List<MenuEntry> readALl() throws IOException;

}
