package testcases;

import common.Constant;
import common.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.HomePage;
import pageobjects.LoginPage;
import pageobjects.RegisterPage;

public class RegisterTest {

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("Pre-condition");
        Constant.WEBDRIVER = new ChromeDriver();
        Constant.WEBDRIVER.manage().window().maximize();
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Post-condition");
        Constant.WEBDRIVER.quit();
    }

    @Test
    public void TC07() {
        System.out.println("TC07 - User can create new account");


        HomePage homePage = new HomePage();
        homePage.open();


        RegisterPage registerPage = homePage.gotoRegisterPage();


        String randomEmail = Utilities.generateRandomEmail();

        registerPage.register(randomEmail, Constant.PASSWORD, Constant.PASSWORD, "123456789");


        String actualMsg = registerPage.getLblRegisterSuccessMsg().getText();
        String expectedMsg = "Thank you for registering your account";


        Assert.assertEquals(actualMsg, expectedMsg, "Register success message is not displayed as expected");
    }

    @Test
    public void TC08() throws InterruptedException {
        System.out.println("TC08 - User can't login with an account hasn't been activated");
        String inactiveEmail = "nguyenthingocanh11254@gmail.com";
        String pass = "NTTT987";


        HomePage homePage = new HomePage();
        homePage.open();


        LoginPage loginPage = homePage.gotoLoginPage();
        loginPage.login(inactiveEmail, pass);


        Thread.sleep(3000);


        String actualErrorMsg = loginPage.getLblLoginErrorMsg().getText().trim();
        String expectedErrorMsg = "Invalid username or password. Please try again.";


        System.out.println("Actual message: " + actualErrorMsg);


        Assert.assertEquals(actualErrorMsg, expectedErrorMsg, "Error: The TC08 error message does not match the Excel requirements!");
    }

    @Test
    public void TC10() {
        System.out.println("TC10 - User can't create account with \"Confirm password\" is not the same with \"Password\"");


        HomePage homePage = new HomePage();
        homePage.open();


        RegisterPage registerPage = homePage.gotoRegisterPage();


        String randomEmail = Utilities.generateRandomEmail();
        String password = Constant.PASSWORD;
        String invalidConfirmPassword = "WrongPassword123";


        registerPage.register(randomEmail, password, invalidConfirmPassword, "123456789");


        String actualMsg = registerPage.getLblRegisterErrorMsg().getText();
        String expectedMsg = "There're errors in the form. Please correct the errors and try again.";


        Assert.assertEquals(actualMsg, expectedMsg, "Error message is not displayed as expected when passwords do not match");
    }

    @Test
    public void TC11() throws InterruptedException {
        System.out.println("TC11 - User can't create account while password and PID fields are empty");


        HomePage homePage = new HomePage();
        homePage.open();


        RegisterPage registerPage = homePage.gotoRegisterPage();


        String validEmail = "test" + System.currentTimeMillis() + "@gmail.com";
        registerPage.register(validEmail, "", "", "");


        Thread.sleep(2000);


        String expectedFormError = "There're errors in the form. Please correct the errors and try again.";
        String expectedPasswordError = "Invalid password length.";
        String expectedPidError = "Invalid ID length.";


        String actualFormError = Constant.WEBDRIVER.findElement(By.xpath("//p[contains(@class, 'message error')]")).getText().trim();
        String actualPasswordError = Constant.WEBDRIVER.findElement(By.xpath("//label[@for='password' and contains(@class, 'validation-error')]")).getText().trim();
        String actualPidError = Constant.WEBDRIVER.findElement(By.xpath("//label[@for='pid' and contains(@class, 'validation-error')]")).getText().trim();


        Assert.assertEquals(actualFormError, expectedFormError, "Form error message is not displayed as expected");
        Assert.assertEquals(actualPasswordError, expectedPasswordError, "Password error message is not displayed as expected");
        Assert.assertEquals(actualPidError, expectedPidError, "PID error message is not displayed as expected");
    }
}

