package utils;

import java.time.Instant;

public final class TestData {
    private TestData() {}

    public static String uniqueEmail() {
        return "ometh_" + Instant.now().getEpochSecond() + "@testmail.com";
    }

    public static final String NAME = "Ometh Test";
    public static final String PASSWORD = "Test@12345";
    public static final String SEARCH_KEYWORD = "dress";
}
