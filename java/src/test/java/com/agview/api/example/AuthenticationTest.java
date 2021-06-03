package com.agview.api.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class AuthenticationTest {

    private final String BASE_URL = System.getenv("NPB_BASE_URL");
    private final String API_KEY = System.getenv("NPB_API_KEY");
    private final String API_SECRET = System.getenv("NPB_API_SECRET");
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    public void authenticates() throws Exception {
        OAuthAccessToken actual = getToken();

        assertThat(actual.getAccess(), is(notNullValue()));
        assertThat(actual.getExp(), is(greaterThan(0L)));
    }

    private OAuthAccessToken getToken() throws Exception{
        Map<String,String> body = new HashMap<>(){{
            put("key", API_KEY);
            put("secret", API_SECRET);
        }};
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyJsonStr = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL+"/auth/org-token/"))
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
    @Disabled
    public void getsIncidents() throws Exception {
        OAuthAccessToken tokens = getToken();
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

    @Test
    public void checksIfTokenWillExpireInTenSeconds() throws Exception {
        OAuthAccessToken accessToken = getToken();

        long expirationTimeInSeconds = accessToken.getExp();
        LocalDateTime expirationTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(expirationTimeInSeconds), ZoneId.systemDefault());

        assertThat(expirationTime.isAfter(LocalDateTime.now().plusSeconds(10)), is(true));
    }
}


