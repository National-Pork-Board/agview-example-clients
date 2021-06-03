package com.agview.api.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class AccessTokenHandlerTest {

    private AccessTokenHandler sut;

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    public void setup() {
        sut = new AccessTokenHandler(new Arguments(BASE_URL, API_KEY, API_SECRET));
    }

    @Test
    public void getsNewAccessToken() {
        OAuthAccessToken actual = sut.getNewAccessToken();

        assertThat(actual.getAccess(), is(notNullValue()));
        assertThat(actual.getExp(), is(greaterThan(0L)));
    }

    @Test
    public void reusesExistingNonExpiredAccessToken() {
        OAuthAccessToken originalAccessToken = sut.getNewAccessToken();

        OAuthAccessToken actual = sut.getNonExpiredOrNewAccessToken(originalAccessToken, 10);

        assertThat(actual, is(originalAccessToken));
    }

    @Test
    public void getsNewAccessTokenWhenExistingOneIsOutsideOfValidityRange() {
        OAuthAccessToken originalAccessToken = sut.getNewAccessToken();

        OAuthAccessToken actual = sut.getNonExpiredOrNewAccessToken(originalAccessToken, 10000);

        assertThat(actual, is(not(originalAccessToken)));
    }

    @Test
    @Disabled
    public void getsIncidents() throws Exception {
        OAuthAccessToken tokens = sut.getNewAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL+"/api/v1/incidents/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+tokens.getExp())
                .GET()
                .build();

        CompletableFuture<HttpResponse> future =
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                      .thenApply(Function.identity());

        HttpResponse httpResponse = future.get();
        assertThat(httpResponse.statusCode(), is(200));
    }


}


