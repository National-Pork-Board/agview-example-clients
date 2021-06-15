package org.npb.agview.api.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovementDbHandler {

    private final String movementFilePath;
    private final String movementAddressesFilePath;
    private final DbHandler dbHandler;

    public MovementDbHandler(String movementFilePath, String movementAddressesFilePath, DbHandler dbHandler) {
        this.movementFilePath = movementFilePath;
        this.movementAddressesFilePath = movementAddressesFilePath;
        this.dbHandler = dbHandler;
    }

    public Collection<Movement> getMovementsToLoad() {
        try (var reader = dbHandler.createReader(movementFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var movements = new ArrayList<Movement>();
            for (CSVRecord record : records) {
                var movement = new Movement();
                movement.setMovementId(Integer.parseInt(record.get("movement_id")));
                movement.setSpecies(record.get("species"));
                movement.setNumberInShipment(Integer.parseInt(record.get("number_in_shipment")));
                movement.setMovementDatetime(record.get("movement_datetime"));
                movement.setMovementType(record.get("movement_type"));
                movements.add(movement);
            }

            return movements;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getOrNull(CSVRecord record, String fieldName) {
        var value = record.get(fieldName);

        return StringUtils.isBlank(value) ? null : value;
    }

    public Collection<MovementAddresses> getMovementsAddressesToLoad() {
        try (var reader = dbHandler.createReader(movementAddressesFilePath)) {
            var records = CSVFormat.EXCEL.withHeader().parse(reader);

            var movementsAddresses = new ArrayList<MovementAddresses>();
            for (CSVRecord record : records) {
                var movementAddress = new MovementAddresses();
                movementAddress.setMovementId(Integer.parseInt(record.get("movement_id")));
                movementAddress.setSource(getOrNull(record, "source"));
                movementAddress.setSourceLatitude(getOrNull(record,  "source_latitude"));
                movementAddress.setSourceLongitude(getOrNull(record,  "source_longitude"));
                movementAddress.setSourceStreetAddress(getOrNull(record, "source_street_address"));
                movementAddress.setSourceCity(getOrNull(record, "source_city"));
                movementAddress.setSourceState(getOrNull(record,"source_state"));
                movementAddress.setSourceZip(getOrNull(record,"source_zip"));

                movementAddress.setTarget(getOrNull(record, "destination"));
                movementAddress.setTargetLatitude(getOrNull(record, "destination_latitude"));
                movementAddress.setTargetLongitude(getOrNull(record, "destination_longitude"));
                movementAddress.setTargetStreetAddress(getOrNull(record, "destination_street_address"));
                movementAddress.setTargetCity(getOrNull(record, "destination_city"));
                movementAddress.setTargetState(getOrNull(record,  "destination_state"));
                movementAddress.setTargetZip(getOrNull(record, "destination_zip"));

                movementsAddresses.add(movementAddress);
            }

            return movementsAddresses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getMovementColumnNames() {
        return dbHandler.getColumnNames(movementFilePath);
    }

    public List<String> getMovementAddressColumnNames() {
        return dbHandler.getColumnNames(movementAddressesFilePath);
    }
}
