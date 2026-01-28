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
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();

        boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));

        ChromeOptions options = new ChromeOptions();

        // Stable defaults (good for local + CI)
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Reduce automation banners/notifications
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        if (isCI) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-popup-blocking");
        }

        driver = new ChromeDriver(options);

        // Timeouts: CI needs more breathing room than local
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // Always try to capture evidence, but never fail teardown because of it
        screenshot(testInfo.getDisplayName());

        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException ignored) {}
        }
    }

    protected void screenshot(String name) {
        try {
            if (driver == null) return;

            Files.createDirectories(new File("screenshots").toPath());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), new File("screenshots/" + safe(name) + ".png").toPath());
        } catch (IOException | WebDriverException ignored) {
            // Intentionally ignore to avoid masking test results
        }
    }

    private String safe(String s) {
        return s.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    protected void hardRefresh() {
        driver.navigate().refresh();
        waitForDomReady();
    }

    protected void waitForUrlContains(String part) {
        wait.until(ExpectedConditions.urlContains(part));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForDomReady() {
        try {
            wait.until(d ->
                    ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete")
            );
        } catch (TimeoutException ignored) {
            // Don't hard fail; some pages keep loading trackers forever in CI
        }
    }
}
