package com.agview.api.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MovementDbHandlerTest {

    private MovementDbHandler sut;

    private final int MOVEMENT_ID1 = 1;
    private final String SPECIES = "Swine";
    private final int NUMBER_IN_SHIPMENT = 10;
    private final String MOVEMENT_DATETIME = "06/08/2021";
    private final String MOVEMENT_TYPE = "Feeder Pigs";

    private final String SOURCE_PREM_ID = "1277XH9";
    private final String TARGET_PREM_ID = "0615PKA";

    @BeforeEach
    public void setup() {
        var projectRoot = System.getProperty("user.dir");
        sut = new MovementDbHandler(projectRoot+"/src/main/resources/movement.csv",
                projectRoot+"/src/main/resources/movement_addresses.csv");
    }

    @Test
    public void getsMovements() {
        var expectedMovement = new Movement(MOVEMENT_ID1, SPECIES, NUMBER_IN_SHIPMENT, MOVEMENT_DATETIME, MOVEMENT_TYPE);

        var actual = sut.getMovementsToLoad();

        assertThat(actual, hasItems(expectedMovement));
    }

    @Test
    public void getsMovementsAddresses() {
        var expectedAddress = new MovementAddresses();
        expectedAddress.setMovementId(MOVEMENT_ID1);
        expectedAddress.setSource(SOURCE_PREM_ID);
        expectedAddress.setTarget(TARGET_PREM_ID);

        var actual = sut.getMovementsAddressesToLoad();

        assertThat(actual, hasItems(expectedAddress));
    }

}
