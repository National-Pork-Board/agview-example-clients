package org.npb.agview.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MovementPostHandlerTest {

    private MovementPostHandler sut;

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String START_DATE = "2021-06-07T13:45";
    private final String END_DATE = "2021-06-08T13:45";
    private final int TOTAL_NUMBER_OF_MOVEMENTS = 9;


    @BeforeEach
    public void setup() {
        var dbHandler = new MovementDbHandler(Constants.DB_DIRECTORY+"/movement.csv",
                Constants.DB_DIRECTORY+"/movement_addresses.csv", new DbHandler());
        var connectionInfo = new Arguments(BASE_URL, API_KEY, API_SECRET);
        var accessTokenHandler = new AccessTokenHandler(httpClient, connectionInfo);
        sut = new MovementPostHandler(httpClient,
                connectionInfo,
                accessTokenHandler,
                dbHandler);
    }

    @Test
    public void createsAllMovements() throws JsonProcessingException {
        var actual = sut.createMovements();

        assertThat(actual, hasSize(TOTAL_NUMBER_OF_MOVEMENTS));
        assertThat(actual.iterator().next().getId(), is(greaterThan(0)));
    }

    @Test
    public void createsMovementsForDateRange() throws JsonProcessingException {
        var actual = sut.createMovementsForDateRange(START_DATE, END_DATE);

        assertThat(actual, not(empty()));
        assertThat(actual, hasSize(lessThan(TOTAL_NUMBER_OF_MOVEMENTS)));
        assertThat(actual.iterator().next().getId(), is(greaterThan(0)));
    }

}
