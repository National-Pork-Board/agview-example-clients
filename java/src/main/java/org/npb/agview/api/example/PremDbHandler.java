package org.npb.agview.api.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PremDbHandler {

    private final String premFilePath;
    private final String premAddressesFilePath;
    private final DbHandler dbHandler;

    public PremDbHandler(String premFilePath, String premAddressesFilePath, DbHandler dbHandler) {
        this.premFilePath = premFilePath;
        this.premAddressesFilePath = premAddressesFilePath;
        this.dbHandler = dbHandler;
    }

    public Collection<Prem> getPremsToLoad() {
        try (var reader = dbHandler.createReader(premFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var prems = new ArrayList<Prem>();
            for (CSVRecord record : records) {
                var prem = new Prem();
                prem.setUsdaPin(record.get("usda_pin"));
                prem.setPremName(record.get("prem_name"));
                prem.setSiteCapacityNumberAnimals(Integer.parseInt(record.get("site_capacity_number_animals")));
                prem.setNumberOfAnimalsOnSite(Integer.parseInt(record.get("number_of_animals_on_site")));
                prem.setSiteCapacityNumberBarns(Integer.parseInt(record.get("site_capacity_number_barns")));
                prem.setIceContactEmail(record.get("ice_contact_email"));
                prem.setIceContactPhone(record.get("ice_contact_phone"));
                prem.setLocationType(record.get("location_type"));
                prem.setSpecies(record.get("species"));

                prems.add(prem);
            }

            return prems;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<PremAddress> getPremAddressesToLoad() {
        try (var reader = dbHandler.createReader(premAddressesFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var premAddresses = new ArrayList<PremAddress>();
            for (CSVRecord record : records) {
                var premAddress = new PremAddress();
                premAddress.setUsdaPin(record.get("usda_pin"));
                premAddress.setStreetAddress(record.get("street_address"));
                premAddress.setCity(record.get("city"));
                premAddress.setState(record.get("state"));
                premAddress.setZip(record.get("zip"));

                premAddresses.add(premAddress);
            }

            return premAddresses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPremColumnNames() {
        return dbHandler.getColumnNames(premFilePath);
    }

    public List<String> getPremAddressesColumnNames() {
        return dbHandler.getColumnNames(premAddressesFilePath);
    }
}
