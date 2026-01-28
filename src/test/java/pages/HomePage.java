package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;

    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By body = By.tagName("body");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://automationexercise.com/");
    }

    public String title() {
        return driver.getTitle();
    }

    public boolean isLoaded() {
        return driver.findElement(body).isDisplayed();
    }

    public void goToSignupLogin() {
        driver.findElement(signupLoginLink).click();
    }

    public void goToProducts() {
        driver.findElement(productsLink).click();
    }

    public void logout() {
        driver.findElement(logoutLink).click();
    }

    public boolean isLogoutVisible() {
        return driver.findElements(logoutLink).size() > 0;
    }
}
