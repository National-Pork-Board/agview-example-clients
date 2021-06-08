package com.agview.api.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovementDbHandler {

    private final String movementFilePath;
    private final String movementAddressesFilePath;

    public MovementDbHandler(String movementFilePath, String movementAddressesFilePath) {
        this.movementFilePath = movementFilePath;
        this.movementAddressesFilePath = movementAddressesFilePath;
    }

    public Collection<Movement> getMovementsToLoad() {
        try (var reader = createReader(movementFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var movements = new ArrayList<Movement>();
            for (CSVRecord record : records) {
                var movement = new Movement();
                movement.setReferenceId(record.get("reference_id"));
                movement.setSpecies(record.get("species"));
                movement.setNumberInShipment(Integer.parseInt(record.get("number_in_shipment")));
                movement.setMovementDatetime(record.get("movement_datetime"));
                movement.setMovementType(record.get("movement_datetime"));
                movements.add(movement);
            }

            return movements;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Reader createReader(String filePath) throws FileNotFoundException {
        return new InputStreamReader(new BOMInputStream(new FileInputStream(filePath)), StandardCharsets.UTF_8);
    }

    public Collection<MovementAddresses> getMovementsAddressesToLoad() {
        try (var reader = createReader(movementAddressesFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var movementsAddresses = new ArrayList<MovementAddresses>();
            for (CSVRecord record : records) {
                var movementAddress = new MovementAddresses();
                movementAddress.setSourceStreetAddress(record.get("source_street_address"));
                movementAddress.setSourceCity(record.get("source_city"));
                movementAddress.setSourceState(record.get("source_state"));
                movementAddress.setSourceZip(record.get("source_zip"));

                movementAddress.setTargetStreetAddress(record.get("destination_street_address"));
                movementAddress.setTargetCity(record.get("destination_city"));
                movementAddress.setTargetState(record.get("destination_state"));
                movementAddress.setTargetZip(record.get("destination_zip"));

                movementsAddresses.add(movementAddress);
            }

            return movementsAddresses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getMovementColumnNames() {
        return getColumnNames(movementFilePath);
    }

    private List<String> getColumnNames(String filePath) {
        try (var reader = createReader(filePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            return records.getHeaderNames();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getMovementAddressesColumnNames() {
        return getColumnNames(movementAddressesFilePath);
    }
}
