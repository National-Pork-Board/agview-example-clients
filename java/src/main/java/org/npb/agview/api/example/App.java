package org.npb.agview.api.example;

import com.beust.jcommander.JCommander;

import java.net.http.HttpClient;
import java.util.Collection;

import static org.npb.agview.api.example.Constants.*;

public class App {

    private static final int ONE_SECOND = 1000;

    public static void main(String[] args) throws InterruptedException {
        args = new String[] {"BASE_URL",BASE_URL,"API_KEY",API_KEY,"API_SECRET",API_SECRET};

        Arguments arguments = processArguments(args);
        var httpClient  = HttpClient.newHttpClient();

        System.out.println("*********Handle Access Token*************************************************");
        var accessTokenHandler = new AccessTokenHandler(HttpClient.newHttpClient(), arguments);
        var accessToken = accessTokenHandler.getNewAccessToken();
        System.out.println("New access token: "+accessToken+"\n");
        Thread.sleep(ONE_SECOND);
        System.out.println("Same access token: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 10)+"\n");
        Thread.sleep(ONE_SECOND);
        System.out.println("New access token due to expiration: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 100000)+"\n");

        System.out.println();
        System.out.println("*********Create Prems Using Multiple Data Sources*************************************************");
        var premDbHandler = new PremDbHandler(DB_DIRECTORY +"/prem.csv",
                DB_DIRECTORY +"/prem_address.csv", new DbHandler());
        var premPostHandler = new PremPostHandler(httpClient, arguments, accessTokenHandler, premDbHandler);
        System.out.println("Combining Prem data");
        System.out.println("\t"+premDbHandler.getPremColumnNames());
        System.out.println("with PremAddress data");
        System.out.println("\t"+premDbHandler.getPremAddressesColumnNames());
        Collection<CreatedPrem> createdPrems = premPostHandler.createPrems();
        System.out.println("Created prems: "+ createdPrems);

        System.out.println();
        System.out.println("*********Create Movements Using Multiple Data Sources*************************************************");
        var movementDbHandler = new MovementDbHandler(DB_DIRECTORY +"/movement.csv",
                DB_DIRECTORY +"/movement_addresses.csv", new DbHandler());
        var movementPostHandler = new MovementPostHandler(httpClient, arguments, accessTokenHandler, movementDbHandler);
        System.out.println("Combining Movement data");
        System.out.println("\t"+movementDbHandler.getMovementColumnNames());
        System.out.println("with Movement Addresses data");
        System.out.println("\t"+movementDbHandler.getMovementAddressesColumnNames());
        System.out.println("Created movements from the entire data: "+movementPostHandler.createMovements());
        var fromDate = "2021-06-07T00:00";
        var toDate = "2021-06-08T23:59";
        System.out.println("Created movements for date range "+fromDate+" thru "+toDate+": "+movementPostHandler.createMovementsForDateRange(fromDate,toDate));

        System.out.println();
        System.out.println("*********Create Lab**********************************************************************************");
        var labPostHandler = new LabPostHandler(httpClient,
                arguments,
                accessTokenHandler);
        System.out.println("Created lab with following response: "+labPostHandler.createLab());
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


