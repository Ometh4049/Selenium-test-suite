package tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.LoginSignupPage;
import pages.ProductsPage;
import utils.TestData;

public class Tests extends BaseTest {

    @Test
    void Task1_OpenWebsite_PrintTitle() {
        // Create HomePage object and open the website
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // Get the title of loaded web page & log it
        String title = homePage.title();
        System.out.println("Website Title: " + title);

        // Verify home page has successfully loaded
        Assertions.assertTrue(homePage.isLoaded(), "Home page should load");

        // Verify that the page title is not empty or blank
        Assertions.assertFalse(title.isBlank(), "Title should not be blank");
    }

    @Test
    void Task2_UserRegistration_VerifySuccess() {
        // Generate unique email address for registration (avoids "email already exists")
        String email = TestData.uniqueEmail();

        // Open the home page
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // Navigate to the Signup / Login page
        homePage.goToSignupLogin();

        // Create LoginSignupPage object to interact with signup elements
        LoginSignupPage page = new LoginSignupPage(driver);

        // Verify that "New User Signup!" section is visible
        Assertions.assertTrue(page.newUserSignupVisible(), "'New User Signup!' should be visible");

        // Start user registration by entering name & email
        page.startSignup(TestData.NAME, email);

        // Verify "Enter Account Information" section is visible
        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");

        // Fill all required registration fields and submit the form
        page.completeSignupRequiredFields(TestData.PASSWORD);

        // Log diagnostics (helps debugging in CI)
        System.out.println("URL after Create Account: " + driver.getCurrentUrl());
        System.out.println("Page title after Create Account: " + driver.getTitle());

        // Verify account created page is shown
        Assertions.assertTrue(page.accountCreatedVisible(), "Expected ACCOUNT CREATED page, but it was not shown");

        // Click Continue after successful account creation
        page.continueAfterCreate();

        // Give CI/headless a moment to complete redirects/navigation after Continue
        waitForUrlContains("automationexercise.com");

        // Some environments do not auto-login after signup (known site behavior)
        if (!homePage.isLoggedIn()) {
            driver.get("https://automationexercise.com/login");
            page.login(email, TestData.PASSWORD);
        }

        // Final verification(Logout link = logged-in state)
        Assertions.assertTrue(
                homePage.isLoggedIn(),
                "User should be logged in after registration"
        );
    }

    @Test
    void Task3_UserLogin_VerifyRedirectedToHome() {
        // 1) Register a fresh user (needed so we always have valid login credentials)
        String email = TestData.uniqueEmail();

        // Open the home page and navigate to Signup / Login page
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.goToSignupLogin();

        // Create LoginSignupPage object to handle signup actions
        LoginSignupPage page = new LoginSignupPage(driver);

        // Start signup using test data
        page.startSignup(TestData.NAME, email);

        // Verify "Enter Account Information" section is visible
        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");

        // Complete all required signup fields and submit
        page.completeSignupRequiredFields(TestData.PASSWORD);

        // Verify account created page is visible
        Assertions.assertTrue(page.accountCreatedVisible(), "User account should be created before login test");

        // Continue after account creation
        page.continueAfterCreate();

        // Give CI/headless a moment to complete redirects/navigation after Continue
        waitForUrlContains("automationexercise.com");

        // Verify user is logged in after registration (CI-safe check)
        Assertions.assertTrue(page.loggedInAsVisible(),
                "User should be logged in after registration (Logged in as OR Logout)");

        // 2) Logout (prefer UI logout, fallback to direct URL)
        if (homePage.isLogoutVisible()) {
            homePage.logout();
        } else {
            driver.get("https://automationexercise.com/logout");
        }

        // 3) Go to login page and login again
        driver.get("https://automationexercise.com/login");
        waitForUrlContains("/login");

        // Log diagnostics (helps debugging in CI)
        System.out.println("Login page URL: " + driver.getCurrentUrl());
        System.out.println("Login page Title: " + driver.getTitle());

        // If login inputs not visible (overlay/redirect issue), refresh once
        if (!page.loginHeaderVisible()) {
            hardRefresh();
        }

        // Verify login input fields are visible
        Assertions.assertTrue(page.loginHeaderVisible(), "Login inputs should be visible");

        // Perform login using previously registered credentials
        page.login(email, TestData.PASSWORD);

        // Verify user is logged in after login
        Assertions.assertTrue(page.loggedInAsVisible(), "User should be logged in successfully after login");
    }

    @Test
    void Task4_ProductSearch_VerifyResults() {
        // Open the home page
        HomePage home = new HomePage(driver);
        home.open();

        // Navigate to products page
        home.goToProducts();

        // Create ProductsPage object to interact with product-related elements
        ProductsPage productsPage = new ProductsPage(driver);

        // Verify that the "ALL PRODUCTS" page is displayed
        Assertions.assertTrue(productsPage.allProductsVisible(), "ALL PRODUCTS page should be visible");

        // Perform product search using predefined keyword
        productsPage.search(TestData.SEARCH_KEYWORD);

        // Verify that the "Searched Products" section is displayed
        Assertions.assertTrue(productsPage.searchedProductsVisible(), "'Searched Products' should be visible");

        // Verify at least one search result contains the keyword
        Assertions.assertTrue(productsPage.anyResultContains(TestData.SEARCH_KEYWORD),
                "At least one product should contain the search keyword");
    }
}
