package utils;

import java.time.Instant;

public final class TestData {
    // Prevent instantiation
    private TestData() {}

    // Generates a unique email for each test run to avoid duplication issues
    public static String uniqueEmail() {
        return "ometh_" + Instant.now().getEpochSecond() + "@testmail.com";
    }

    // Common test constants
    public static final String NAME = "Ometh Test";
    public static final String PASSWORD = "Test@12345";
    public static final String SEARCH_KEYWORD = "dress";
}
