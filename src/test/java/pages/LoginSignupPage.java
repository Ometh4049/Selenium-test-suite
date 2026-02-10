package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Set;

public class LoginSignupPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By newUserSignupHeader = By.xpath("//h2[contains(.,'New User Signup')]");
    private final By signupName = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmail = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");

    private final By enterAccountInfoHeader = By.xpath("//b[contains(.,'Enter Account Information')]");
    private final By titleMr = By.id("id_gender1");
    private final By password = By.cssSelector("input[data-qa='password']");
    private final By days = By.id("days");
    private final By months = By.id("months");
    private final By years = By.id("years");

    private final By firstName = By.id("first_name");
    private final By lastName = By.id("last_name");
    private final By address1 = By.id("address1");
    private final By country = By.id("country");
    private final By state = By.id("state");
    private final By city = By.id("city");
    private final By zipcode = By.id("zipcode");
    private final By mobileNumber = By.id("mobile_number");

    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

    private final By accountCreatedHeader =
            By.xpath("//b[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ACCOUNT CREATED')]");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    private final By loginEmail = By.cssSelector("input[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By loggedInAs = By.xpath("//a[contains(.,'Logged in as')]");

    // Common ad close button (site sometimes shows overlays)
    private final By adClose = By.cssSelector(".modal .close, .close-modal, .btn-close");

    public LoginSignupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public boolean newUserSignupVisible() {
        return visible(newUserSignupHeader).isDisplayed();
    }

    public void startSignup(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
        clickable(signupButton).click();
        visible(enterAccountInfoHeader);
    }

    public boolean enterAccountInfoVisible() {
        return visible(enterAccountInfoHeader).isDisplayed();
    }

    public void completeSignupRequiredFields(String pwd) {
        visible(enterAccountInfoHeader);

        clickable(titleMr).click();
        type(password, pwd);

        new Select(visible(days)).selectByValue("10");
        new Select(visible(months)).selectByValue("5");
        new Select(visible(years)).selectByValue("2002");

        type(firstName, "Ometh");
        type(lastName, "Test");
        type(address1, "No 123, Main Street");
        new Select(visible(country)).selectByVisibleText("India");

        type(state, "Western");
        type(city, "Colombo");
        type(zipcode, "10000");
        type(mobileNumber, "771234567");

        try {
            clickable(createAccountButton).click();
        } catch (Exception e) {
            jsClick(visible(createAccountButton));
        }
    }

    public boolean accountCreatedVisible() {
        try {
            visible(accountCreatedHeader);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void continueAfterCreate() {
        // Sometimes "Continue" opens a new tab or is blocked by an overlay.
        String originalWindow = driver.getWindowHandle();
        Set<String> before = driver.getWindowHandles();

        // Try to close overlay if present
        tryCloseOverlay();

        // Try normal click, then JS click
        try {
            clickable(continueButton).click();
        } catch (Exception e) {
            try {
                WebElement btn = driver.findElement(continueButton);
                jsClick(btn);
            } catch (Exception ignored) {}
        }

        // If new tab opened, switch back / close it
        Set<String> after = driver.getWindowHandles();
        if (after.size() > before.size()) {
            for (String w : after) {
                if (!w.equals(originalWindow)) {
                    driver.switchTo().window(w);
                    driver.close();
                }
            }
            driver.switchTo().window(originalWindow);
        }

        // If we’re still stuck, just go to home to stabilize state
        driver.navigate().to("https://automationexercise.com/");
    }

    private void tryCloseOverlay() {
        try {
            WebElement close = driver.findElement(adClose);
            jsClick(close);
        } catch (Exception ignored) {}
    }

    public boolean loginHeaderVisible() {
        try {
            visible(loginEmail);
            visible(loginPassword);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void login(String email, String pwd) {
        type(loginEmail, email);
        type(loginPassword, pwd);
        clickable(loginButton).click();
    }

    public boolean loggedInAsVisible() {
        try {
            visible(loggedInAs);
            return true;
        } catch (TimeoutException e) {
            // Fallback: logout link exists => logged in
            return driver.findElements(By.cssSelector("a[href='/logout']")).size() > 0;
        }
    }
}
