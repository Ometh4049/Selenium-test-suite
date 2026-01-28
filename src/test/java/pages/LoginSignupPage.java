package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginSignupPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Signup/account info locators ---
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

    // --- Login locators ---
    private final By loginEmail = By.cssSelector("input[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");

    private final By loggedInAs = By.xpath("//a[contains(.,'Logged in as')]");

    public LoginSignupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ---------- Helpers ----------
    private WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void jsClick(By locator) {
        WebElement el = visible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    // ---------- Signup ----------
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
        } catch (ElementClickInterceptedException | TimeoutException e) {
            jsClick(createAccountButton);
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
        try {
            clickable(continueButton).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            jsClick(continueButton);
        }
    }

    // ---------- Login ----------
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
            return false;
        }
    }
}
