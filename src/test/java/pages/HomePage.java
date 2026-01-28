package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String BASE_URL = "https://automationexercise.com/";

    // Navigation links
    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By productsLink = By.cssSelector("a[href='/products']");

    // A stable “home loaded” signal: top nav exists
    private final By headerNav = By.cssSelector("header, .header-middle, .shop-menu, nav");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        driver.get(BASE_URL);
        // Ensure page is actually ready for interactions
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(headerNav),
                ExpectedConditions.presenceOfElementLocated(signupLoginLink),
                ExpectedConditions.presenceOfElementLocated(productsLink)
        ));
    }

    public String title() {
        return driver.getTitle();
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(headerNav),
                    ExpectedConditions.presenceOfElementLocated(signupLoginLink),
                    ExpectedConditions.presenceOfElementLocated(productsLink)
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void goToSignupLogin() {
        safeClick(signupLoginLink);
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    public void goToProducts() {
        safeClick(productsLink);
        wait.until(ExpectedConditions.urlContains("/products"));
    }

    public void logout() {
        // Prefer UI logout if visible; sometimes intercept happens
        safeClick(logoutLink);
        // after logout, site often redirects home or login
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/login"),
                ExpectedConditions.urlToBe(BASE_URL),
                ExpectedConditions.presenceOfElementLocated(signupLoginLink)
        ));
    }

    public boolean isLoggedIn() {
        return driver.findElements(logoutLink).size() > 0;
    }

    public boolean isLogoutVisible() {
        return driver.findElements(logoutLink).size() > 0;
    }

    // ---------- helpers ----------
    private void safeClick(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
                return;
            } catch (ElementClickInterceptedException | TimeoutException e) {
                // JS fallback when overlays/ads block the click
                try {
                    WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (WebDriverException ignored) {
                    // retry
                }
            }
        }
        // Last resort: throw meaningful error
        throw new TimeoutException("Unable to click element: " + locator);
    }
}
