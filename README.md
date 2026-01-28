# selenium-test-suite Software Testing & Quality Assurance

## 📌 Project Overview
This project is a Selenium WebDriver automation test suite developed as part of the  
**Software Testing & Quality Assurance** course assignment.

The automation validates core user functionalities of a sample web application using:
- Java
- Selenium WebDriver
- JUnit 5
- Maven
- Page Object Model (POM) design pattern

## ✅ Website Under Test
Automation Exercise: https://automationexercise.com/

## 🧰 Tools & Technologies
- **Programming Language:** Java (JDK 21)
- **Automation Tool:** Selenium WebDriver 4
- **Test Framework:** JUnit 5
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA
- **Browser:** Google Chrome
- **Driver Management:** WebDriverManager

## 📌 Test Cases Implemented
1. **Task1_OpenWebsite_PrintTitle**  
   Opens the website, maximizes window, prints title, validates page load.

2. **Task2_UserRegistration_VerifySuccess**  
   Registers a user with dummy data and verifies account creation + logged-in status.

3. **Task3_UserLogin_VerifyRedirectedToHome**  
   Logs out after registration, then logs in again and verifies logged-in status.

4. **Task4_ProductSearch_VerifyResults**  
   Searches for a product keyword and validates results include the keyword.

## ✅ Automated Test Cases
1. Open Website and Verify Title
2. User Registration and Success Validation
3. User Login and Dashboard Verification
4. Product Search and Result Validation
5. Browser Closure after Each Test

## 📋 Test Case Mapping Table

| Test Case ID | Test Scenario | Manual Test Steps | Expected Result | Automated Test Method |
|-------------|--------------|------------------|-----------------|-----------------------|
| TC-01 | Open Website | 1. Launch browser<br>2. Enter site URL | Homepage loads with title | `Task1_OpenWebsite_PrintTitle()` |
| TC-02 | User Registration | 1. Open Signup page<br>2. Enter valid name, email, password<br>3. Submit form | Account created successfully | `Task2_UserRegistration_VerifySuccess()` |
| TC-03 | User Login | 1. Navigate to Login page<br>2. Enter valid credentials<br>3. Submit login | User redirected to homepage | `Task3_UserLogin_VerifyRedirectedToHome()` |
| TC-04 | Product Search | 1. Go to Products page<br>2. Search for product | Matching products displayed | `Task4_ProductSearch_VerifyResults()` |
| TC-05 | Browser Close | End test execution | Browser closes properly | `@AfterEach → tearDown()` |

---

## ▶️ How to Run
### Option 1: Run from IntelliJ IDEA
1. Open the project in IntelliJ
2. Open `src/test/java/tests/Tests.java`
3. Click the green ▶️ run button

### Option 2: Run using Maven (Terminal)
```bash
mvn clean test


