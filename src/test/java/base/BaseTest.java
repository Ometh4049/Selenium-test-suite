package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.PageLoadStrategy;
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

        ChromeOptions options = new ChromeOptions();

        // Prefer NORMAL locally to reduce "blank/white" during eager navigation
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // Stability flags
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // If you ever run headless again, white screens reduce with these:
        // options.addArguments("--headless=new");
        // options.addArguments("--window-size=1920,1080");
        // options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // Optional: keep a final always
        screenshot(testInfo.getDisplayName() + "__FINAL");
        if (driver != null) driver.quit();
    }

    // Use this for navigation to reduce renderer timeouts
    protected void safeGet(String url) {
        try {
            driver.navigate().to(url);
            waitForPageStable();
        } catch (TimeoutException e) {
            // stop loading then continue
            try { ((JavascriptExecutor) driver).executeScript("window.stop();"); } catch (Exception ignored) {}
            driver.navigate().to(url);
            waitForPageStable();
        }
    }

    protected void waitForUrlContains(String part) {
        wait.until(ExpectedConditions.urlContains(part));
    }

    protected void hardRefresh() {
        driver.navigate().refresh();
        waitForPageStable();
    }

    //  Click helper (no invalid multi-catch)
    protected void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.click();
        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    //  Call this before taking screenshots to avoid WHITE images
    protected void waitForPageStable() {
        // 1) DOM ready
        wait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")
        ));

        // 2) Body visible (ensures something is painted)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        // 3) tiny settle time (helps on ad-heavy pages)
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }

    //  Take a step screenshot: START / END, etc.
    protected void stepScreenshot(String testName, String step) {
        waitForPageStable();
        screenshot(testName + "__" + step);
    }

    protected void screenshot(String name) {
        try {
            Files.createDirectories(new File("screenshots").toPath());
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), new File("screenshots/" + safe(name) + ".png").toPath());
        } catch (IOException | WebDriverException ignored) {}
    }

    private String safe(String s) {
        return s.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
