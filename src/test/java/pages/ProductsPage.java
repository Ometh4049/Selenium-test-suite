package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page elements related to products and search
    private final By allProductsHeader = By.xpath("//h2[contains(.,'All Products')]");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchedProductsHeader = By.xpath("//h2[contains(.,'Searched Products')]");
    private final By productNames = By.cssSelector(".productinfo p");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Verifies that the "All Products" page is visible
    public boolean allProductsVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(allProductsHeader)).isDisplayed();
    }

    // Performs a product search using the given keyword
    public void search(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(keyword);

        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    // Verifies that the "Searched Products" section is displayed
    public boolean searchedProductsVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchedProductsHeader)).isDisplayed();
    }

    // Checks whether at least one product name contains the search keyword
    public boolean anyResultContains(String keyword) {
        List<WebElement> names = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
        return names.stream().anyMatch(e -> e.getText().toLowerCase().contains(keyword.toLowerCase()));
    }
}
