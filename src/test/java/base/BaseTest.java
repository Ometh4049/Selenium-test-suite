package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Runs before each test case
    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();

        boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));

        ChromeOptions options = new ChromeOptions();

        if (isCI) {
            // Headless for GitHub Actions/Linux runner
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }


    // Runs after each test case
    @AfterEach
    void tearDown(TestInfo testInfo) {
        // Capture screenshot for test evidence
        screenshot(testInfo.getDisplayName());

        // Close browser safely
        if (driver != null) driver.quit();
    }

    // Save screenshots automatically
    protected void screenshot(String name) {
        try {
            Files.createDirectories(new File("screenshots").toPath());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), new File("screenshots/" + safe(name) + ".png").toPath());
        } catch (IOException | WebDriverException ignored) {
            // Ignore failures to avoid masking test results
        }
    }

    // Makes file names OS-safe
    private String safe(String s) {
        return s.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    // Forces a full page reload (useful for flaky pages)
    protected void hardRefresh() {
        driver.navigate().refresh();
    }

    // Waits until URL contains expected value
    protected void waitForUrlContains(String part) {
        wait.until(ExpectedConditions.urlContains(part));
    }
}
