package repository;

import java.io.*;
import java.util.List;

//this could be interface for just MenuEntry, but I was trying to recreate CRUD, with little to non success
public interface Repository<T>{
    void saveAll(List<T> t) throws IOException;
    List<T> readALl() throws IOException;

}
