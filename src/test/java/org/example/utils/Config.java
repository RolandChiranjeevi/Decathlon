package org.example.utils;

public final class Config {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080/";

    private Config() {
    }

    /**
     * Where the web UI is running. Defaults to the local Spring Boot app,
     * can be overridden with the BASE_URL environment variable.
     */
    public static String baseUrl() {
        return System.getenv().getOrDefault("BASE_URL", DEFAULT_BASE_URL);
    }
}
