package testcases;

import common.Constant;
import common.Utilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.ChangePasswordPage;
import pageobjects.HomePage;
import pageobjects.LoginPage;
import pageobjects.GeneralPage;
import pageobjects.RegisterPage;

public class ChangePasswordTest {
    @BeforeMethod
    public void beforeMethod() {
        System.out.println("Pre-condition");
        Constant.WEBDRIVER = new ChromeDriver();
        Constant.WEBDRIVER.manage().window().maximize();
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Post-condition");
        if (Constant.WEBDRIVER != null) {
            Constant.WEBDRIVER.quit();
        }
    }

    @Test
    public void TC09() {
        System.out.println("TC09 - User can change password");

        String email = Utilities.generateRandomEmail();
        String originalPassword = Constant.PASSWORD;
        String temporaryPassword = originalPassword + "Tmp1!";
        String pid = "123456789";

        HomePage homePage = new HomePage();
        homePage.open();

        // 1. Register a new account
        RegisterPage registerPage = homePage.gotoRegisterPage();
        registerPage.register(email, originalPassword, originalPassword, pid);

        // 2. Login with the new account
        LoginPage loginPage = registerPage.gotoLoginPage();
        GeneralPage generalPage = loginPage.login(email, originalPassword);

        // 3. Change password
        ChangePasswordPage changePasswordPage = generalPage.gotoChangePasswordPage();
        changePasswordPage.changePassword(originalPassword, temporaryPassword, temporaryPassword);

        String actualMsg = changePasswordPage.getLblSuccessMessage().getText().trim();
        String expectedMsg = "Your password has been updated";

        Assert.assertEquals(actualMsg, expectedMsg, "Change password success message is not displayed as expected");

        // 4. Verify login with new password
        generalPage.getTabLogout().click();
        loginPage = homePage.gotoLoginPage();
        generalPage = loginPage.login(email, temporaryPassword);

        Assert.assertTrue(generalPage.isLogoutTabDisplayed(), "User cannot login with the new password");
    }
}