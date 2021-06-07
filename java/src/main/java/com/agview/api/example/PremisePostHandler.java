package com.agview.api.example;

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

public class PremisePostHandler {

    private final HttpClient httpClient;
    private final Arguments connectionInfo;
    private final AccessTokenHandler accessTokenHandler;
    private final DbHandler dbHandler;

    public PremisePostHandler(HttpClient httpClient,
                              Arguments connectionInfo,
                              AccessTokenHandler accessTokenHandler,
                              DbHandler dbHandler) {
        this.httpClient = httpClient;
        this.connectionInfo = connectionInfo;
        this.accessTokenHandler = accessTokenHandler;
        this.dbHandler = dbHandler;
    }

    public CreatedPremise[] createPremises() {
        try {
            Collection<Premise> premises = dbHandler.getPremisesToLoad();
            Collection<PremiseAddress> premiseAddresses = dbHandler.getPremiseAddressesToLoad();

            //        var projectRoot = System.getProperty("user.dir");
            //        var dbHandler = new DbHandler(projectRoot+"/src/main/resources/premise.csv",
            //                projectRoot+"/src/main/resources/premise_address.csv");

            var premiseAddressByUsdaPin = premiseAddresses.stream().collect(Collectors.toMap(PremiseAddress::getUsdaPin, Function.identity()));
            var foo = new HashMap<String, Object>() {{

            }};
            var postRequestBody = premises.stream().map(premise ->
                    new HashMap<String, Object>() {
                        {
                            put("usdaPin", premise.getUsdaPin());
                            put("premName", premise.getPremName());
                            put("species", premise.getSpecies());
                            put("iceContactPhone", premise.getIceContactPhone());
                            put("iceContactEmail", premise.getIceContactEmail());
                            put("locationType", premise.getLocationType());
                            put("siteCapacityNumberBarns", premise.getSiteCapacityNumberBarns());
                            put("siteCapacityNumberAnimals", premise.getSiteCapacityNumberAnimals());
                            put("numberOfAnimalsOnSite", premise.getNumberOfAnimalsOnSite());
                            put("streetAddress", premiseAddressByUsdaPin.get(premise.getUsdaPin()).getStreetAddress());
                            put("city", premiseAddressByUsdaPin.get(premise.getUsdaPin()).getCity());
                            put("state", premiseAddressByUsdaPin.get(premise.getUsdaPin()).getState());
                            put("zip", premiseAddressByUsdaPin.get(premise.getUsdaPin()).getZip());
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
                    new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

            return responseObjectMapper.readValue(future.get(), CreatedPremise[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
