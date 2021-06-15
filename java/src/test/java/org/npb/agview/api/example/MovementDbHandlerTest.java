package org.npb.agview.api.example;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.npb.agview.api.example.Constants.*;

class MovementDbHandlerTest {

    private MovementDbHandler sut;

    private final int MOVEMENT_ID1 = 1;
    private final String SPECIES = "Swine";
    private final int NUMBER_IN_SHIPMENT = 10;
    private final String MOVEMENT_DATETIME = "2021-06-08T13:45";
    private final String MOVEMENT_TYPE = "Feeder Pigs";

    private final String SOURCE_PREM_ID = "1277XH9";
    private final String TARGET_PREM_ID = "0615PKA";

    @BeforeEach
    public void setup() {
        sut = new MovementDbHandler(DB_DIRECTORY+"/movement.csv",
                DB_DIRECTORY+"/movement_addresses.csv", new DbHandler());
    }

    @Test
    public void getsMovements() {
        var expectedMovement = new Movement(MOVEMENT_ID1, SPECIES, NUMBER_IN_SHIPMENT, MOVEMENT_DATETIME, MOVEMENT_TYPE);

        var actual = sut.getMovementsToLoad();

        assertThat(actual, Matchers.hasItems(expectedMovement));
    }

    @Test
    public void getsMovementColumnNames() {
        var actual = sut.getMovementColumnNames();

        assertThat(actual, contains("movement_id","species","number_in_shipment","movement_datetime","movement_type"));
    }

    @Test
    public void getsMovementsAddresses() {
        var expectedAddress = new MovementAddresses();
        expectedAddress.setMovementId(MOVEMENT_ID1);
        expectedAddress.setSource(SOURCE_PREM_ID);
        expectedAddress.setTarget(TARGET_PREM_ID);

        var actual = sut.getMovementsAddressesToLoad();

        assertThat(actual, Matchers.hasItems(expectedAddress));
    }

    @Test
    public void getsMovementAddressColumnNames() {
        var actual = sut.getMovementAddressColumnNames();

        assertThat(actual, contains("movement_id","source","source_latitude","source_longitude","source_street_address","source_city","source_state","source_zip","destination","destination_latitude","destination_longitude","destination_street_address","destination_city","destination_state","destination_zip"));
    }

}
