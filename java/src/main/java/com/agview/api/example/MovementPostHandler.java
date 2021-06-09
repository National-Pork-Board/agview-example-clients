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

public class MovementPostHandler {

    private final HttpClient httpClient;
    private final Arguments connectionInfo;
    private final AccessTokenHandler accessTokenHandler;
    private final MovementDbHandler movementDbHandler;

    public MovementPostHandler(HttpClient httpClient,
                               Arguments connectionInfo,
                               AccessTokenHandler accessTokenHandler,
                               MovementDbHandler movementDbHandler) {
        this.httpClient = httpClient;
        this.connectionInfo = connectionInfo;
        this.accessTokenHandler = accessTokenHandler;
        this.movementDbHandler = movementDbHandler;
    }

    public CreatedMovement[] createMovements() {
        try {
            Collection<Movement> movements = movementDbHandler.getMovementsToLoad();
            Collection<MovementAddresses> movementsAddresses = movementDbHandler.getMovementsAddressesToLoad();

            var movementAddressesByMovementId = movementsAddresses.stream().collect(Collectors.toMap(MovementAddresses::getMovementId, Function.identity()));

            var postRequestBody = movements.stream().map(movement ->
                    new HashMap<String, Object>() {
                        {
                            put("species", movement.getSpecies());
                            put("numberInShipment", movement.getNumberInShipment());
                            put("movementType", movement.getMovementType());
                            put("movementDatetime", movement.getMovementDatetime());
                            put("source", movementAddressesByMovementId.get(movement.getMovementId()).getSource());
                            put("sourceLatitude", movementAddressesByMovementId.get(movement.getMovementId()).getSourceLatitude());
                            put("sourceLongitude", movementAddressesByMovementId.get(movement.getMovementId()).getSourceLongitude());
                            put("sourceAddress", new HashMap<String, String>() {{
                                put("streetAddress", movementAddressesByMovementId.get(movement.getMovementId()).getSourceStreetAddress());
                                put("city", movementAddressesByMovementId.get(movement.getMovementId()).getSourceCity());
                                put("state", movementAddressesByMovementId.get(movement.getMovementId()).getSourceState());
                                put("zip", movementAddressesByMovementId.get(movement.getMovementId()).getSourceZip());
                            }});
                            put("destination", movementAddressesByMovementId.get(movement.getMovementId()).getTarget());
                            put("destinationLatitude", movementAddressesByMovementId.get(movement.getMovementId()).getTargetLatitude());
                            put("destinationLongitude", movementAddressesByMovementId.get(movement.getMovementId()).getTargetLongitude());
                            put("destinationAddress", new HashMap<String, String>() {{
                                put("streetAddress", movementAddressesByMovementId.get(movement.getMovementId()).getTargetStreetAddress());
                                put("city", movementAddressesByMovementId.get(movement.getMovementId()).getTargetCity());
                                put("state", movementAddressesByMovementId.get(movement.getMovementId()).getTargetState());
                                put("zip", movementAddressesByMovementId.get(movement.getMovementId()).getTargetZip());
                            }});
                        }
                    }).collect(Collectors.toList());

            var objectMapper = new ObjectMapper();
            var bodyJsonStr = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(postRequestBody);
            var accessToken = accessTokenHandler.getNewAccessToken();
            var request = HttpRequest.newBuilder()
                    .uri(new URI(connectionInfo.getBaseUrl() + "/api/v1/movements/"))
                    .headers("Content-Type", "application/json",
                            "Authorization", "Bearer "+accessToken.getAccess())
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJsonStr))
                    .build();
            var future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body);
            var responseObjectMapper =
                    new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return responseObjectMapper.readValue(future.get(), CreatedMovement[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
