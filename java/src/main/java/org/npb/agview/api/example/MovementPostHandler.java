package org.npb.agview.api.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public Collection<CreatedMovement> createMovements() {
        try {
            Collection<Movement> movements = movementDbHandler.getMovementsToLoad();
            Collection<MovementAddresses> movementsAddresses = movementDbHandler.getMovementsAddressesToLoad();

            var movementAddressesByMovementId = movementsAddresses.stream().collect(Collectors.toMap(MovementAddresses::getMovementId, Function.identity()));

            var postRequestBody =
                    movements.stream().map(movement -> createRequestBody(movementAddressesByMovementId, movement))
                            .collect(Collectors.toList());

            return postMovements(postRequestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, Object> createRequestBody(java.util.Map<Integer, MovementAddresses> movementAddressesByMovementId, Movement movement) {
        var source = movementAddressesByMovementId.get(movement.getMovementId()).getSource();
        var sourceLatitude = movementAddressesByMovementId.get(movement.getMovementId()).getSourceLatitude();
        var target = movementAddressesByMovementId.get(movement.getMovementId()).getTarget();
        var targetLatitude = movementAddressesByMovementId.get(movement.getMovementId()).getTargetLatitude();

        var requestBody = new HashMap<String, Object>();
        requestBody.put("species", movement.getSpecies());
        requestBody.put("numberInShipment", movement.getNumberInShipment());
        requestBody.put("movementType", movement.getMovementType());
        requestBody.put("movementDatetime", movement.getMovementDatetime());
        requestBody.put("source", movementAddressesByMovementId.get(movement.getMovementId()).getSource());
        requestBody.put("sourceLatitude", movementAddressesByMovementId.get(movement.getMovementId()).getSourceLatitude());
        requestBody.put("sourceLongitude", movementAddressesByMovementId.get(movement.getMovementId()).getSourceLongitude());
        if (source == null && sourceLatitude == null) {
            requestBody.put("sourceAddress", new HashMap<String, String>() {{
                put("streetAddress", movementAddressesByMovementId.get(movement.getMovementId()).getSourceStreetAddress());
                put("city", movementAddressesByMovementId.get(movement.getMovementId()).getSourceCity());
                put("state", movementAddressesByMovementId.get(movement.getMovementId()).getSourceState());
                put("zip", movementAddressesByMovementId.get(movement.getMovementId()).getSourceZip());
            }});
        }
        requestBody.put("destination", movementAddressesByMovementId.get(movement.getMovementId()).getTarget());
        requestBody.put("destinationLatitude", movementAddressesByMovementId.get(movement.getMovementId()).getTargetLatitude());
        requestBody.put("destinationLongitude", movementAddressesByMovementId.get(movement.getMovementId()).getTargetLongitude());
        if (target == null && targetLatitude == null) {
            requestBody.put("destinationAddress", new HashMap<String, String>() {{
                put("streetAddress", movementAddressesByMovementId.get(movement.getMovementId()).getTargetStreetAddress());
                put("city", movementAddressesByMovementId.get(movement.getMovementId()).getTargetCity());
                put("state", movementAddressesByMovementId.get(movement.getMovementId()).getTargetState());
                put("zip", movementAddressesByMovementId.get(movement.getMovementId()).getTargetZip());
            }});
        }
        return requestBody;
    }

    private Collection<CreatedMovement> postMovements(java.util.List<HashMap<String, Object>> postRequestBody) throws JsonProcessingException, URISyntaxException, InterruptedException, java.util.concurrent.ExecutionException {
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

        return responseObjectMapper.readValue(future.get(), new TypeReference<>(){});
    }

    public Collection<CreatedMovement> createMovementsForDateRange(String startTimestamp, String endTimestamp) {
        try {
            Collection<Movement> movements = movementDbHandler.getMovementsToLoad();
            Collection<MovementAddresses> movementsAddresses = movementDbHandler.getMovementsAddressesToLoad();

            var movementAddressesByMovementId =
                    movementsAddresses.stream()
                            .collect(Collectors.toMap(MovementAddresses::getMovementId, Function.identity()));

            var postRequestBody =
                    movements.stream()
                            .filter(movementsWithinTimerange(startTimestamp, endTimestamp))
                            .map(movement -> createRequestBody(movementAddressesByMovementId, movement))
                            .collect(Collectors.toList());

            return postMovements(postRequestBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Predicate<Movement> movementsWithinTimerange(String startTimestampStr, String endTimestampStr) {
        return movement -> {
            var movementTimeStr = movement.getMovementDatetime();
            LocalDateTime movementTime = LocalDateTime.parse(movementTimeStr);
            LocalDateTime startTimestamp = LocalDateTime.parse(startTimestampStr);
            LocalDateTime endTimestamp = LocalDateTime.parse(endTimestampStr);

            return isGreaterThanOrEqualTo(movementTime, startTimestamp)
                    && isLessThanOrEqualTo(movementTime, endTimestamp);
        };
    }

    private boolean isGreaterThanOrEqualTo(LocalDateTime timeToCheck, LocalDateTime cutoffTime) {
        return timeToCheck.isEqual(cutoffTime) || timeToCheck.isAfter(cutoffTime);
    }

    private boolean isLessThanOrEqualTo(LocalDateTime timeToCheck, LocalDateTime cutoffTime) {
        return timeToCheck.isEqual(cutoffTime) || timeToCheck.isBefore(cutoffTime);
    }

}
