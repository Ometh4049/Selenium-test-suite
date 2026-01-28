package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // IMPORTANT: don't mix implicit + explicit waits
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        screenshot(testInfo.getDisplayName());
        if (driver != null) driver.quit();
    }

    // Save screenshots automatically
    protected void screenshot(String name) {
        try {
            Files.createDirectories(new File("screenshots").toPath());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), new File("screenshots/" + safe(name) + ".png").toPath());
        } catch (IOException | WebDriverException ignored) {
        }
    }

    private String safe(String s) {
        return s.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    protected void hardRefresh() {
        driver.navigate().refresh();
    }

    protected void waitForUrlContains(String part) {
        wait.until(ExpectedConditions.urlContains(part));
    }
}
