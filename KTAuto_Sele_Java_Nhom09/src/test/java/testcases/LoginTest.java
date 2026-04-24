package testcases;

import common.Constant;
import common.Utilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.HomePage;
import pageobjects.LoginPage;
import pageobjects.GeneralPage;

public class LoginTest {


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
    public void TC01 () {
        System.out.println("TC01 - User can log into Railway with valid username and password");


        HomePage homePage = new HomePage();
        homePage.open();


        LoginPage loginPage = homePage.gotoLoginPage();


        String actualMsg = loginPage.login(Constant.USERNAME, Constant.PASSWORD)
                .getWelcomeMessage();


        String expectedMsg = "Welcome " + Constant.USERNAME;


        Utilities.sleep(4000);


        Assert.assertTrue(homePage.isLogoutTabDisplayed(), "Logout is not displayed");
        Assert.assertEquals(actualMsg, expectedMsg,
                "Welcome message is not displayed as expected");
    }

    @Test
    public void TC02() throws InterruptedException {
        System.out.println("TC02 - User can't login with blank 'Username' textbox");
        HomePage homePage = new HomePage();
        homePage.open();


        LoginPage loginPage = homePage.gotoLoginPage();

        loginPage.login("", Constant.PASSWORD);
        String actualErrorMsg = loginPage.getLblLoginErrorMsg().getText().trim();

        String expectedErrorMsg = "There was a problem with your login and/or errors exist in your form. ";

        System.out.println("Actual Error: " + actualErrorMsg);

        Assert.assertEquals(actualErrorMsg, expectedErrorMsg, "Error: The error message displayed does not match the Excel requirements!");
    }

    @Test
    public void TC03() {
        System.out.println("TC03 - User cannot log into Railway with invalid password");


        HomePage homePage = new HomePage();
        homePage.open();


        LoginPage loginPage = homePage.gotoLoginPage();
        loginPage.login(Constant.USERNAME, "invalid_password_123");


        String actualMsg = loginPage.getLblLoginErrorMsg().getText();
        String expectedMsg = "There was a problem with your login and/or errors exist in your form.";


        Assert.assertEquals(actualMsg, expectedMsg, "Error message is not displayed as expected");
    }
    @Test
    public void TC04() throws InterruptedException {
        System.out.println("TC04 - Login page displays when un-logged User clicks on 'Book ticket' tab");

        HomePage homePage = new HomePage();
        homePage.open();

        homePage.gotoBookTicketPage();

        Thread.sleep(2000);

        String currentUrl = Constant.WEBDRIVER.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("Login"), "Error: Not redirected to the Login page!");
    }

    @Test
    public void TC05() throws InterruptedException {
        System.out.println("TC05 - System shows message when user enters wrong password several times");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        // Chuỗi password sai
        String invalidPass = "WrongPass123!";

        // Lặp lại việc đăng nhập sai 4 lần
        for (int i = 1; i <= 4; i++) {
            System.out.println("Attempt #" + i + ": Logging in with wrong password...");
            loginPage.login(Constant.USERNAME, invalidPass);
            Thread.sleep(1000); // Đợi 1 giây giữa các lần thử
        }

        // Sau lần thứ 4 (hoặc 5 tuỳ hệ thống), kiểm tra thông báo lỗi
        Thread.sleep(3000); // Chờ trang tải lại thông báo

        String actualMsg = loginPage.getLblLoginErrorMsg().getText().trim();
        String expectedMsg = "You have used 4 out of 5 login attempts. After all 5 have been used, you will be unable to login for 15 minutes.";

        System.out.println("Actual message on web: " + actualMsg);

        Assert.assertEquals(actualMsg, expectedMsg, "Error: The TC05 error message does not match the Excel requirements!");
    }

    @Test
    public void TC06() {
        System.out.println("TC06 - Additional pages display once user logged in");

        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();
        GeneralPage generalPage = loginPage.login("trangnguyen12345@gmail.com", "trang12345");
        Assert.assertTrue(generalPage.getTabMyTicket().isDisplayed(), "\"My ticket\" tab is not displayed");
        Assert.assertTrue(generalPage.getTabChangePassword().isDisplayed(), "\"Change password\" tab is not displayed");
        Assert.assertTrue(generalPage.getTabLogout().isDisplayed(), "\"Logout\" tab is not displayed");

        generalPage.getTabMyTicket().click();
        String currentUrl = Constant.WEBDRIVER.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/Page/ManageTicket.cshtml"),
                "User is not directed to My ticket page. Current URL: " + currentUrl);

        Constant.WEBDRIVER.navigate().back();

        generalPage.getTabChangePassword().click();
        currentUrl = Constant.WEBDRIVER.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/Account/ChangePassword.cshtml"),
                "User is not directed to Change password page. Current URL: " + currentUrl);
    }}
