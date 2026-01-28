package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;

    // Navigation links on the home page
    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By productsLink = By.cssSelector("a[href='/products']");

    // Used to verify that the page has loaded
    private final By body = By.tagName("body");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // open web page
    public void open() {
        driver.get("https://automationexercise.com/");
    }

    // return title of current page
    public String title() {
        return driver.getTitle();
    }

    // verify page has loaded successfully
    public boolean isLoaded() {
        return driver.findElement(body).isDisplayed();
    }

    // Navigates to the Signup / Login page
    public void goToSignupLogin() {
        driver.findElement(signupLoginLink).click();
    }

    // Navigates to the Products page
    public void goToProducts() {
        driver.findElement(productsLink).click();
    }

    // Logs out the currently logged-in user
    public void logout() {
        driver.findElement(logoutLink).click();
    }

    public boolean isLoggedIn() {
        return driver.findElements(logoutLink).size() > 0;
    }


    // Checks whether the Logout option is visible
    public boolean isLogoutVisible() {
        return driver.findElements(logoutLink).size() > 0;
    }
}
