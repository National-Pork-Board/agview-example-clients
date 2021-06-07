package com.agview.api.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DbHandler {

    private final String premisesFilePath;
    private final String premiseAddressesFilePath;

    public DbHandler(String premisesFilePath, String premiseAddressesFilePath) {
        this.premisesFilePath = premisesFilePath;
        this.premiseAddressesFilePath = premiseAddressesFilePath;
    }

    public Collection<Premise> getPremisesToLoad() {
        try (var reader = createReader(premisesFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var premises = new ArrayList<Premise>();
            for (CSVRecord record : records) {
                var premise = new Premise();
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

    private Reader createReader(String filePath) throws FileNotFoundException {
        return new InputStreamReader(new BOMInputStream(new FileInputStream(filePath)), StandardCharsets.UTF_8);
    }

    public Collection<PremiseAddress> getPremiseAddressesToLoad() {
        try (var reader = createReader(premiseAddressesFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var premiseAddresses = new ArrayList<PremiseAddress>();
            for (CSVRecord record : records) {
                var premiseAddress = new PremiseAddress();
                premiseAddress.setUsdaPin(record.get("usda_pin"));
                premiseAddress.setStreetAddress(record.get("street_address"));
                premiseAddress.setCity(record.get("city"));
                premiseAddress.setState(record.get("state"));
                premiseAddress.setZip(record.get("zip"));

                premiseAddresses.add(premiseAddress);
            }

            return premiseAddresses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPremisesColumnNames() {
        return getColumnNames(premisesFilePath);
    }

    private List<String> getColumnNames(String filePath) {
        try (var reader = createReader(filePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            return records.getHeaderNames();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPremiseAddressesColumnNames() {
        return getColumnNames(premiseAddressesFilePath);
    }
}
