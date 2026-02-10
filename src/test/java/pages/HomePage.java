package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By body = By.tagName("body");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void open() {
        driver.navigate().to("https://automationexercise.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(body));
    }

    public String title() {
        return driver.getTitle();
    }

    public boolean isLoaded() {
        return driver.findElement(body).isDisplayed();
    }

    public void goToSignupLogin() {
        clickRobust(signupLoginLink);
    }

    public void goToProducts() {
        clickRobust(productsLink);
    }

    public void logout() {
        clickRobust(logoutLink);
    }

    public boolean isLogoutVisible() {
        return driver.findElements(logoutLink).size() > 0;
    }

    private void clickRobust(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
