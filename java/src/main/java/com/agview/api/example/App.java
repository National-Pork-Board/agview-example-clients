package com.agview.api.example;

import com.beust.jcommander.JCommander;

import java.net.http.HttpClient;

import static com.agview.api.example.Constants.*;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Arguments arguments = processArguments(args);
        var httpClient  = HttpClient.newHttpClient();
        var dbHandler = new PremiseDbHandler(PROJECT_ROOT+"/src/main/resources/premise.csv",
                PROJECT_ROOT+"/src/main/resources/premise_address.csv");

        System.out.println("*********Handle Access Token*************************************************");
        var accessTokenHandler = new AccessTokenHandler(HttpClient.newHttpClient(), arguments);
        var accessToken = accessTokenHandler.getNewAccessToken();
        System.out.println("New access token: "+accessToken+"\n");
        Thread.sleep(1000);
        System.out.println("Same access token: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 10)+"\n");
        Thread.sleep(1000);
        System.out.println("New access token due to expiration: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 100000)+"\n");

        System.out.println("");
        System.out.println("*********Create Premise Using Multiple CSV Files*************************************************");
        var premisePostHandler = new PremisePostHandler(httpClient, arguments, accessTokenHandler, dbHandler);
        System.out.println("Combining Premise table");
        System.out.println("\t"+dbHandler.getPremisesColumnNames());
        System.out.println("with PremiseAddress table");
        System.out.println("\t"+dbHandler.getPremiseAddressesColumnNames());
        CreatedPremise[] createdPremises = premisePostHandler.createPremises();
        System.out.println("Created premise with ID: "+createdPremises[0].getId());
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


