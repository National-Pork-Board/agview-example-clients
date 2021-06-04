package com.agview.api.example;

import com.beust.jcommander.JCommander;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new App().getGreeting());

        var arguments = new Arguments();
        var argumentParser = JCommander
                .newBuilder()
                .addObject(arguments)
                .programName("AgView API Tutorial")
                .build();

        argumentParser.parse(args);

        var accessTokenHandler = new AccessTokenHandler(arguments);
        var accessToken = accessTokenHandler.getNewAccessToken();
        System.out.println("New access token: "+accessToken+"\n");
        Thread.sleep(1000);
        System.out.println("Same access token: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 10)+"\n");
        Thread.sleep(1000);
        System.out.println("New access token due to expiration: "+accessTokenHandler.getNonExpiredOrNewAccessToken(accessToken, 100000)+"\n");
    }
}


