# selenium-test-suite (SQA Assignment 02)

Automated UI test suite using **Java + Selenium WebDriver + JUnit 5**.

## ✅ Website Under Test
Automation Exercise: https://automationexercise.com/

## 🧰 Tech Stack
- Java 21
- Selenium WebDriver
- WebDriverManager
- JUnit 5
- IntelliJ IDEA (Community)

## 📌 Test Cases Implemented
1. **Task1_OpenWebsite_PrintTitle**  
   Opens the website, maximizes window, prints title, validates page load.

2. **Task2_UserRegistration_VerifySuccess**  
   Registers a user with dummy data and verifies account creation + logged-in status.

3. **Task3_UserLogin_VerifyRedirectedToHome**  
   Logs out after registration, then logs in again and verifies logged-in status.

4. **Task4_ProductSearch_VerifyResults**  
   Searches for a product keyword and validates results include the keyword.

## ▶️ How to Run
### Option 1: Run from IntelliJ IDEA
1. Open the project in IntelliJ
2. Open `src/test/java/tests/Tests.java`
3. Click the green ▶️ run button

### Option 2: Run using Maven (Terminal)
```bash
mvn clean test
