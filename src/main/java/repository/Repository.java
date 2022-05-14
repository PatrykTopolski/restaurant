package repository;

import java.io.*;
import java.util.List;
public interface Repository<T>{
    void saveAll(List<T> t) throws IOException;
    List<T> readALl() throws IOException;

}
