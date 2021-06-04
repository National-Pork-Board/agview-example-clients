package com.agview.api.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class DbHandler {

    private final String csvFilePath;

    public DbHandler(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public Collection<Premise> getPremisesToLoad() {
        try (Reader in = new InputStreamReader(new BOMInputStream(new FileInputStream(csvFilePath)), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

            Collection<Premise> premises = new ArrayList<>();
            for (CSVRecord record : records) {
                Premise premise = new Premise();
                premise.setUsdaPin(record.get("usda_pin"));
                premise.setPremName(record.get("prem_name"));
                premise.setSiteCapacityNumberAnimals(Integer.parseInt(record.get("site_capacity_number_animals")));
                premise.setNumberOfAnimalsOnSite(Integer.parseInt(record.get("number_of_animals_on_site")));
                premise.setSiteCapacityNumberBarns(Integer.parseInt(record.get("site_capacity_number_barns")));
                premise.setIceContactEmail(record.get("ice_contact_email"));
                premise.setIceContactPhone(record.get("ice_contact_phone"));
                premise.setLocationType(record.get("location_type"));
                premise.setSpecies(record.get("species"));

                premises.add(premise);
            }

            return premises;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
