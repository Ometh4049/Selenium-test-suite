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
        // create home page obj and open the website
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // get the title of loaded web page & log it
        String title = homePage.title();
        System.out.println("Website Title: " + title);

        // verify home page has successfully loaded
        Assertions.assertTrue(homePage.isLoaded(), "Home page should load");

        // Verify that the page title is not empty or blank
        Assertions.assertFalse(title.isBlank(), "Title should not be blank");
    }

    @Test
    void Task2_UserRegistration_VerifySuccess() {
        // gen unique email address for reg..
        String email = TestData.uniqueEmail();

        // create home page obj and open the website
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // Navigate to the Signup / Login page
        homePage.goToSignupLogin();

        // Create LoginSignupPage obj to interact with signup elements
        LoginSignupPage page = new LoginSignupPage(driver);
        //verify that signup works correctly
        Assertions.assertTrue(page.newUserSignupVisible(), "'New User Signup!' should be visible");

        // start user reg by entering name & email
        page.startSignup(TestData.NAME, email);
        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");

        // Fill all required registration fields and submit the form
        page.completeSignupRequiredFields(TestData.PASSWORD);

        // log the URL & page title
        System.out.println("URL after Create Account: " + driver.getCurrentUrl());
        System.out.println("Page title after Create Account: " + driver.getTitle());

        // verify acc has create successfully
        Assertions.assertTrue(page.accountCreatedVisible(), "Expected ACCOUNT CREATED page, but it was not shown");

        // Click Continue after successful account creation
        page.continueAfterCreate();

        // verify that signup by login
        Assertions.assertTrue(page.loggedInAsVisible(), "Should show 'Logged in as ...' after registration");
    }

    @Test
    void Task3_UserLogin_VerifyRedirectedToHome() {
        // 1) Register a fresh user
        String email = TestData.uniqueEmail();

        // Open the home page and navigate to Signup / Login page
        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.goToSignupLogin();

        // Create LoginSignupPage obj to handle signup actions
        LoginSignupPage page = new LoginSignupPage(driver);
        // Start the signup process using test data
        page.startSignup(TestData.NAME, email);

        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");
        // Complete all required signup fields and submit the form
        page.completeSignupRequiredFields(TestData.PASSWORD);

        // Verify that the account has been successfully created
        Assertions.assertTrue(page.accountCreatedVisible(), "User account should be created before login test");

        // continue after acc creation
        page.continueAfterCreate();

        // Verify that the user is logged in immediately after registration
        Assertions.assertTrue(page.loggedInAsVisible(), "User should be logged in after registration");

        //STEP 2) Logout
        if (homePage.isLogoutVisible()) {
            homePage.logout();
        } else {
            driver.get("https://automationexercise.com/logout");
        }

        // 3) Go to login page and login again
        driver.get("https://automationexercise.com/login");
        waitForUrlContains("/login");

        System.out.println("Login page URL: " + driver.getCurrentUrl());
        System.out.println("Login page Title: " + driver.getTitle());

        // If login inputs not visible (overlay issue), refresh once
        if (!page.loginHeaderVisible()) {
            hardRefresh();
        }

        // Verify that login input fields are visible
        Assertions.assertTrue(page.loginHeaderVisible(), "Login inputs should be visible");

        // Perform login using previously registered credentials
        page.login(email, TestData.PASSWORD);

        Assertions.assertTrue(page.loggedInAsVisible(), "User should be logged in successfully after login");
    }

    @Test
    void Task4_ProductSearch_VerifyResults() {
        // Open the home page of the application
        HomePage home = new HomePage(driver);
        home.open();

        // navigate to products page
        home.goToProducts();

        // Create ProductsPage obj to interact with product-related elements
        ProductsPage productsPage = new ProductsPage(driver);

        // Verify that the "ALL PRODUCTS" page is displayed
        Assertions.assertTrue(productsPage.allProductsVisible(), "ALL PRODUCTS page should be visible");

        // Perform product search using a predefined keyword
        productsPage.search(TestData.SEARCH_KEYWORD);

        // Verify that the "Searched Products" section is displayed
        Assertions.assertTrue(productsPage.searchedProductsVisible(), "'Searched Products' should be visible");

        // Verify that at least one search result contains the search keyword
        Assertions.assertTrue(productsPage.anyResultContains(TestData.SEARCH_KEYWORD),
                "At least one product should contain the search keyword");
    }
}
