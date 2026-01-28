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
    void Task1_OpenWebsite_PrintTitle(){
        HomePage homePage = new HomePage(driver);
        homePage.open();

        System.out.println("Website Title : " + homePage.title());

        Assertions.assertTrue(homePage.isLoaded(), "Home page should load");
        Assertions.assertFalse(homePage.title().isBlank(), "Title should not be blank");
    }

    @Test
    void Task2_UserRegistration_VerifySuccess(){
        String email = TestData.uniqueEmail();

        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.goToSignupLogin();

        LoginSignupPage page = new LoginSignupPage(driver);
        Assertions.assertTrue(page.newUserSignupVisible(), "'New User Signup!' should be visible");

        page.startSignup(TestData.NAME, email);
        Assertions.assertTrue(page.enterAccountInfoVisible(), "'Enter Account Information' should be visible");

        page.completeSignupMinimal(TestData.PASSWORD);
        Assertions.assertTrue(page.accountCreatedVisible(), "'ACCOUNT CREATED!' should be visible");

        page.continueAfterCreate();
        Assertions.assertTrue(page.loggedInAsVisible(), "Should show 'Logged in as ...' after registration");
    }

    @Test
    void Task3_UserLogin_VerifyRedirectedToHome(){

        String email = TestData.uniqueEmail();

        HomePage homePage = new HomePage(driver);
        homePage.open();
        homePage.goToSignupLogin();

        LoginSignupPage loginSignupPage = new LoginSignupPage(driver);
        loginSignupPage.startSignup(TestData.NAME,email);
        loginSignupPage.completeSignupMinimal(TestData.PASSWORD);
        loginSignupPage.continueAfterCreate();

        driver.get("https://automationexercise.com/login");

        Assertions.assertTrue(loginSignupPage.loginHeaderVisible(), "'Login to your account' should be visible");
        loginSignupPage.login(email, TestData.PASSWORD);

        Assertions.assertTrue(loginSignupPage.loggedInAsVisible(), "User should be logged in successfully");

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
                "At least one product should contain search keyword");
    }
}
