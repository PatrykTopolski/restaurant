package repository;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import model.menu.MenuEntry;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MenuRepositoryImpl implements Repository<MenuEntry> {
    String RESOURCE_PATH = "./target/generated-sources/menu.csv";

    @Override
    public void saveAll(List<MenuEntry> t) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.typedSchemaFor(MenuEntry.class )
                .withHeader()
                .withColumnSeparator(',')
                .withoutQuoteChar()
                .withColumnSeparator(',')
                .withoutQuoteChar();
        ObjectWriter myObjectWriter = mapper.writer(schema);
        File tempFile = new File(RESOURCE_PATH);
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8);
        myObjectWriter.writeValue(writerOutputStream, t);
        System.out.println("saved menu");
    }

    @Override
    public List<MenuEntry> readALl() throws IOException {
        File csvFile = new File(RESOURCE_PATH);
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper
                .typedSchemaFor(MenuEntry.class)
                .withHeader()
                .withColumnSeparator(',')
                .withoutQuoteChar()
                .withColumnSeparator(',')
                .withoutQuoteChar();
        MappingIterator<MenuEntry> menuEntryIterator = csvMapper
                .readerWithTypedSchemaFor(MenuEntry.class)
                .with(csvSchema)
                .readValues(csvFile);
        System.out.println("loaded menu from file");
        return menuEntryIterator.readAll();
    }





}
