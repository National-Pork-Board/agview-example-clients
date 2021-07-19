package org.npb.agview.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.npb.agview.api.example.Constants.*;

class LabPostHandlerTest {

    private LabPostHandler sut;

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    public void setup() {
        var connectionInfo = new Arguments(BASE_URL, API_KEY, API_SECRET);
        var accessTokenHandler = new AccessTokenHandler(httpClient, connectionInfo);
        sut = new LabPostHandler(httpClient,
                connectionInfo,
                accessTokenHandler);
    }

    @Test
    public void createsPrems() throws JsonProcessingException {
        var actual = sut.createLab();

        assertThat(actual, containsString("ACK_R25"));
    }

}
