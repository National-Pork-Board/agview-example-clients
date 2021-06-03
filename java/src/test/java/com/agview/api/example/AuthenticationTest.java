package com.agview.api.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


class AuthenticationTest {

    private final String BASE_URL = "http://localhost:8080";
    private final String USER_NAME = "saho@gmail.com";
    private final String PASSWORD = "password";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    public void authenticates() throws Exception {
        OAuthAccessToken actual = getTokens();

        assertThat(actual.getAccess(), is(notNullValue()));
        assertThat(actual.getRefresh(), is(notNullValue()));
    }

    private OAuthAccessToken getTokens() throws Exception{
        Map<String,String> body = new HashMap<>(){{
            put("email", USER_NAME);
            put("password", PASSWORD);
        }};
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyJsonStr = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL+"/auth/token/"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJsonStr))
                .build();

        CompletableFuture<HttpResponse> future =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(Function.identity());

        HttpResponse httpResponse = future.get();

        return extractTokensFrom(httpResponse);
    }

    private OAuthAccessToken extractTokensFrom(HttpResponse httpResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(httpResponse.body().toString(), OAuthAccessToken.class);
    }

    @Test
    public void getsIncidents() throws Exception {
        OAuthAccessToken tokens = getTokens();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL+"/api/v1/incidents/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+tokens.getAccess())
                .GET()
                .build();

        CompletableFuture<HttpResponse> future =
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                      .thenApply(Function.identity());

        HttpResponse httpResponse = future.get();
        assertThat(httpResponse.statusCode(), is(200));
    }
}


