package org.npb.agview.api.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PremPostHandler {

    private final HttpClient httpClient;
    private final Arguments connectionInfo;
    private final AccessTokenHandler accessTokenHandler;
    private final PremDbHandler premDbHandler;

    public PremPostHandler(HttpClient httpClient,
                           Arguments connectionInfo,
                           AccessTokenHandler accessTokenHandler,
                           PremDbHandler premDbHandler) {
        this.httpClient = httpClient;
        this.connectionInfo = connectionInfo;
        this.accessTokenHandler = accessTokenHandler;
        this.premDbHandler = premDbHandler;
    }

    public Collection<CreatedPrem> createPrems() {
        try {
            var prems = premDbHandler.getPremsToLoad();
            var premsAddresses = premDbHandler.getPremAddressesToLoad();

            var premAddressByUsdaPin = premsAddresses.stream().collect(Collectors.toMap(PremAddress::getUsdaPin, Function.identity()));

            var postRequestBody = prems.stream().map(prem ->
                    new HashMap<String, Object>() {
                        {
                            put("usdaPin", prem.getUsdaPin());
                            put("premName", prem.getPremName());
                            put("species", prem.getSpecies());
                            put("iceContactPhone", prem.getIceContactPhone());
                            put("iceContactEmail", prem.getIceContactEmail());
                            put("locationType", prem.getLocationType());
                            put("siteCapacityNumberBarns", prem.getSiteCapacityNumberBarns());
                            put("siteCapacityNumberAnimals", prem.getSiteCapacityNumberAnimals());
                            put("numberOfAnimalsOnSite", prem.getNumberOfAnimalsOnSite());
                            put("streetAddress", premAddressByUsdaPin.get(prem.getUsdaPin()).getStreetAddress());
                            put("city", premAddressByUsdaPin.get(prem.getUsdaPin()).getCity());
                            put("state", premAddressByUsdaPin.get(prem.getUsdaPin()).getState());
                            put("zip", premAddressByUsdaPin.get(prem.getUsdaPin()).getZip());
                        }
                    }
            )
                    .collect(Collectors.toList());

            var objectMapper = new ObjectMapper();
            var bodyJsonStr = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(postRequestBody);
            var accessToken = accessTokenHandler.getNewAccessToken();
            var request = HttpRequest.newBuilder()
                    .uri(new URI(connectionInfo.getBaseUrl() + "/api/v1/prems/"))
                    .headers("Content-Type", "application/json",
                            "Authorization", "Bearer "+accessToken.getAccess())
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJsonStr))
                    .build();
            var future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body);
            var responseObjectMapper =
                    new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return responseObjectMapper.readValue(future.get(), new TypeReference<>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
