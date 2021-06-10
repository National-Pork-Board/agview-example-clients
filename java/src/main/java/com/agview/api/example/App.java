package com.agview.api.example;

import com.beust.jcommander.JCommander;

import java.net.http.HttpClient;
import java.util.Collection;

import static com.agview.api.example.Constants.*;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Arguments arguments = processArguments(args);
        var httpClient  = HttpClient.newHttpClient();

        System.out.println("*********Handle Access Token*************************************************");
        var accessTokenHandler = new AccessTokenHandler(HttpClient.newHttpClient(), arguments);
        var accessToken = accessTokenHandler.getNewAccessToken();
        System.out.println("New access token: "+accessToken+"\n");
        Thread.sleep(1000);
        System.out.println("Same access token: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 10)+"\n");
        Thread.sleep(1000);
        System.out.println("New access token due to expiration: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 100000)+"\n");

        System.out.println();
        System.out.println("*********Create Premises Using Multiple CSV Files*************************************************");
        var premiseDbHandler = new PremiseDbHandler(PROJECT_ROOT+"/src/main/resources/premise.csv",
                PROJECT_ROOT+"/src/main/resources/premise_address.csv", new DbHandler());
        var premisePostHandler = new PremisePostHandler(httpClient, arguments, accessTokenHandler, premiseDbHandler);
        System.out.println("Combining Premise table");
        System.out.println("\t"+premiseDbHandler.getPremisesColumnNames());
        System.out.println("with PremiseAddress table");
        System.out.println("\t"+premiseDbHandler.getPremiseAddressesColumnNames());
        Collection<CreatedPremise> createdPremises = premisePostHandler.createPremises();
        System.out.println("Created premises: "+createdPremises);

        System.out.println();
        System.out.println("*********Create Movements Using Multiple CSV Files*************************************************");
        var movementDbHandler = new MovementDbHandler(PROJECT_ROOT+"/src/main/resources/movement.csv",
                PROJECT_ROOT+"/src/main/resources/movement_addresses.csv", new DbHandler());
        var movementPostHandler = new MovementPostHandler(httpClient, arguments, accessTokenHandler, movementDbHandler);
        System.out.println("Combining Movement table");
        System.out.println("\t"+movementDbHandler.getMovementColumnNames());
        System.out.println("with MovementAddresses table");
        System.out.println("\t"+movementDbHandler.getMovementAddressesColumnNames());
        Collection<CreatedMovement> createdMovements = movementPostHandler.createMovements();
        System.out.println("Created movements: "+createdMovements);
    }

    private static Arguments processArguments(String[] args) {
        var arguments = new Arguments();
        var argumentParser = JCommander
                .newBuilder()
                .addObject(arguments)
                .programName("AgView API Tutorial")
                .build();
        argumentParser.parse(args);

        return arguments;
    }
}


