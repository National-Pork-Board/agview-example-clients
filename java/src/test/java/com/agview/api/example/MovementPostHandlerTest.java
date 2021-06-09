package com.agview.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

class MovementPostHandlerTest {

    private MovementPostHandler sut;

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String PROJECT_ROOT = System.getProperty("user.dir");

    @BeforeEach
    public void setup() {
        var dbHandler = new MovementDbHandler(PROJECT_ROOT+"/src/main/resources/movement.csv",
                PROJECT_ROOT+"/src/main/resources/movement_addresses.csv");
        var connectionInfo = new Arguments(BASE_URL, API_KEY, API_SECRET);
        var accessTokenHandler = new AccessTokenHandler(httpClient, connectionInfo);
        sut = new MovementPostHandler(httpClient,
                connectionInfo,
                accessTokenHandler,
                dbHandler);
    }

    @Test
    public void createsPremises() throws JsonProcessingException {
        var actual = sut.createMovements();

        assertThat(actual[0].getId(), is(greaterThan(0)));
    }

}
