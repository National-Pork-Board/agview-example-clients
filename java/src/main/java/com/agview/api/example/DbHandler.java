package com.agview.api.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.input.BOMInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DbHandler {
    public Reader createReader(String filePath) throws FileNotFoundException {
        return new InputStreamReader(new BOMInputStream(new FileInputStream(filePath)), StandardCharsets.UTF_8);
    }

    public List<String> getColumnNames(String filePath) {
        try (var reader = createReader(filePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            return records.getHeaderNames();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
