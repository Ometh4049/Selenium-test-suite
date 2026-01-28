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
        HomePage homePage = new HomePage(driver);
        homePage.open();

        String title = homePage.title();
        System.out.println("Website Title: " + title);

        Assertions.assertTrue(homePage.isLoaded(), "Home page should load");
        Assertions.assertFalse(title.isBlank(), "Title should not be blank");
    }

    @Test
    void Task2_UserRegistration_VerifySuccess() {
        String email = TestData.uniqueEmail();

        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.goToSignupLogin();

        LoginSignupPage page = new LoginSignupPage(driver);
        Assertions.assertTrue(page.newUserSignupVisible(), "'New User Signup!' should be visible");

        page.startSignup(TestData.NAME, email);
        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");

        page.completeSignupRequiredFields(TestData.PASSWORD);

        System.out.println("URL after Create Account: " + driver.getCurrentUrl());
        System.out.println("Page title after Create Account: " + driver.getTitle());

        Assertions.assertTrue(page.accountCreatedVisible(), "Expected ACCOUNT CREATED page, but it was not shown");

        page.continueAfterCreate();
        Assertions.assertTrue(page.loggedInAsVisible(), "Should show 'Logged in as ...' after registration");
    }

    @Test
    void Task3_UserLogin_VerifyRedirectedToHome() {
        // 1) Register a fresh user
        String email = TestData.uniqueEmail();

        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.goToSignupLogin();

        LoginSignupPage page = new LoginSignupPage(driver);
        page.startSignup(TestData.NAME, email);

        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");
        page.completeSignupRequiredFields(TestData.PASSWORD);

        Assertions.assertTrue(page.accountCreatedVisible(), "User account should be created before login test");
        page.continueAfterCreate();
        Assertions.assertTrue(page.loggedInAsVisible(), "User should be logged in after registration");

        // 2) Logout
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

        Assertions.assertTrue(page.loginHeaderVisible(), "Login inputs should be visible");
        page.login(email, TestData.PASSWORD);

        Assertions.assertTrue(page.loggedInAsVisible(), "User should be logged in successfully after login");
    }

    @Test
    void Task4_ProductSearch_VerifyResults() {
        HomePage home = new HomePage(driver);
        home.open();
        home.goToProducts();

        ProductsPage productsPage = new ProductsPage(driver);
        Assertions.assertTrue(productsPage.allProductsVisible(), "ALL PRODUCTS page should be visible");

        productsPage.search(TestData.SEARCH_KEYWORD);

        Assertions.assertTrue(productsPage.searchedProductsVisible(), "'Searched Products' should be visible");
        Assertions.assertTrue(productsPage.anyResultContains(TestData.SEARCH_KEYWORD),
                "At least one product should contain the search keyword");
    }
}
