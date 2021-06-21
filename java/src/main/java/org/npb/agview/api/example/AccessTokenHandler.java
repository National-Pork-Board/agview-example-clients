package org.npb.agview.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.function.Function;

public class AccessTokenHandler {


    private final HttpClient httpClient;
    private final Arguments connectionInfo;

    public AccessTokenHandler(HttpClient httpClient, Arguments connectionInfo) {
        this.httpClient = httpClient;
        this.connectionInfo = connectionInfo;
    }


    public AccessToken getNewAccessToken() {
        try {
            var body = new HashMap<>() {{
                put("key", connectionInfo.getApiKey());
                put("secret", connectionInfo.getApiSecret());
            }};
            var objectMapper = new ObjectMapper();
            var bodyJsonStr = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(body);
            var request = HttpRequest.newBuilder()
                    .uri(new URI(connectionInfo.getBaseUrl() + "/auth/org-token/"))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJsonStr))
                    .build();

            var future =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(Function.identity());

            var httpResponse = future.get();

            return extractTokenFrom(httpResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AccessToken extractTokenFrom(HttpResponse httpResponse) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();

        return objectMapper.readValue(httpResponse.body().toString(), AccessToken.class);
    }

    public AccessToken getNonExpiredOrNewAccessToken(AccessToken accessToken,
                                                     long minimumValidityLeftInSeconds) {
        var expirationTimeInSeconds = accessToken.getExp();
        var expirationTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(expirationTimeInSeconds), ZoneId.systemDefault());

       if(expirationTime.isAfter(LocalDateTime.now().plusSeconds(minimumValidityLeftInSeconds))) {
           return accessToken;
       }

       return getNewAccessToken();
    }
}
