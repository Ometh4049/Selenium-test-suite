package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage {
    public final WebDriver driver;

    private final By allProductsHeader = By.xpath("//h2[contains(.,'All Products')]");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchedProductsHeader = By.xpath("//h2[contains(.,'Searched Products')]");
    private final By productNames = By.cssSelector(".productinfo p");


    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean allProductsVisible() {
        return driver.findElement(allProductsHeader).isDisplayed();
    }

    public void search(String keyword) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(keyword);
        driver.findElement(searchButton).click();
    }

    public boolean searchedProductsVisible() {
        return driver.findElement(searchedProductsHeader).isDisplayed();
    }

    public boolean anyResultContains(String keyword) {
        List<WebElement> names = driver.findElements(productNames);
        return names.stream().anyMatch(e -> e.getText().toLowerCase().contains(keyword.toLowerCase()));
    }
}
