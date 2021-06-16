package org.npb.agview.api.example;

import java.nio.file.Paths;

public class Constants {
    public static final String DB_DIRECTORY = Paths.get(System.getProperty("user.dir")).getParent().toString()+"/db";
    public static final String BASE_URL = System.getenv("NPB_BASE_URL");
    public static final String API_KEY = System.getenv("NPB_API_KEY");
    public static final String API_SECRET = System.getenv("NPB_API_SECRET");
}
