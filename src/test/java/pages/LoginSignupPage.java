package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginSignupPage {
    private final WebDriver driver;

    private final By newUserSignupHeader = By.xpath("//h2[contains(.,'New User Signup')]");
    private final By signupName = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmail = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("button[data-qa='signup-button']");

    private final By enterAccountInfoHeader = By.xpath("//b[contains(.,'Enter Account Information')]");
    private final By password = By.cssSelector("input[data-qa='password']");
    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

    private final By accountCreatedHeader = By.xpath("//*[contains(.,'ACCOUNT CREATED!')]");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    private final By loginHeader = By.xpath("//h2[contains(.,'Login to your account')]");
    private final By loginEmail = By.cssSelector("input[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");

    private final By loggedInAs = By.xpath("//a[contains(.,'Logged in as')]");

    public LoginSignupPage(WebDriver driver){
        this.driver = driver;
    }

    public boolean newUserSignupVisible() {
        return driver.findElement(newUserSignupHeader).isDisplayed();
    }

    public void startSignup(String name, String email) {
        driver.findElement(signupName).sendKeys(name);
        driver.findElement(signupEmail).sendKeys(email);
        driver.findElement(signupButton).click();
    }

    public boolean enterAccountInfoVisible() {
        return driver.findElement(enterAccountInfoHeader).isDisplayed();
    }

    public void completeSignupMinimal(String pwd) {
        driver.findElement(password).sendKeys(pwd);
        driver.findElement(createAccountButton).click();
    }

    public boolean accountCreatedVisible() {
        return driver.findElement(accountCreatedHeader).isDisplayed();
    }

    public void continueAfterCreate() {
        driver.findElement(continueButton).click();
    }

    public boolean loginHeaderVisible() {
        return driver.findElement(loginHeader).isDisplayed();
    }

    public void login(String email, String pwd) {
        driver.findElement(loginEmail).sendKeys(email);
        driver.findElement(loginPassword).sendKeys(pwd);
        driver.findElement(loginButton).click();
    }

    public boolean loggedInAsVisible() {
        return driver.findElement(loggedInAs).isDisplayed();
    }
}
