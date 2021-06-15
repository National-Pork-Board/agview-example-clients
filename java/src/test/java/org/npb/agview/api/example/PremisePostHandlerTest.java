package org.npb.agview.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.npb.agview.api.example.Constants.*;

class PremisePostHandlerTest {

    private PremisePostHandler sut;

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    public void setup() {
        var dbHandler = new PremiseDbHandler(DB_DIRECTORY+"/premise.csv",
                DB_DIRECTORY+"/premise_address.csv", new DbHandler());
        var connectionInfo = new Arguments(BASE_URL, API_KEY, API_SECRET);
        var accessTokenHandler = new AccessTokenHandler(httpClient, connectionInfo);
        sut = new PremisePostHandler(httpClient,
                connectionInfo,
                accessTokenHandler,
                dbHandler);
    }

    @Test
    public void createsPremises() throws JsonProcessingException {
        var actual = sut.createPremises();

        assertThat(actual, hasSize(greaterThan(0)));
    }

}
