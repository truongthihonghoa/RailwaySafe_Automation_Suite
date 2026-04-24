package testcases;

import common.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.HomePage;

public class ResetPasswordTest {
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
    public void TC12() throws InterruptedException {
        System.out.println("TC12: Show error when password reset token is blank");

        String registeredEmail = "hongduyen161005@gmail.com";

        HomePage homePage = new HomePage();

        homePage.open();
        homePage.gotoLoginPage();

        Constant.WEBDRIVER.findElement(By.xpath("//a[@href='/Account/ForgotPassword.cshtml']")).click();
        Thread.sleep(2000);

        Constant.WEBDRIVER.findElement(By.id("email")).sendKeys(registeredEmail);

        WebElement sendBtn = Constant.WEBDRIVER.findElement(By.xpath("//input[@value='Send Instructions']"));
        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].click();", sendBtn);
        Thread.sleep(3000);

        String resetUrl = Constant.RAILWAY_URL.replace("Page/HomePage.cshtml", "Account/PasswordReset.cshtml");
        Constant.WEBDRIVER.navigate().to(resetUrl);
        Thread.sleep(2000);

        Constant.WEBDRIVER.findElement(By.id("newPassword")).sendKeys("NewPassword123!");
        Constant.WEBDRIVER.findElement(By.id("confirmPassword")).sendKeys("NewPassword123!");
        Constant.WEBDRIVER.findElement(By.id("resetToken")).clear();

        WebElement resetBtn = Constant.WEBDRIVER.findElement(By.xpath("//input[@value='Reset Password']"));
        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].click();", resetBtn);
        Thread.sleep(2000);

        String expectedFormError = "The password reset token is incorrect or may be expired. Visit the forgot password page to generate a new one.";
        String actualFormError = Constant.WEBDRIVER.findElement(By.xpath("//p[contains(@class, 'message error')]")).getText().trim();
        Assert.assertEquals(actualFormError, expectedFormError, "Form error message is not displayed as expected");

        String expectedTokenError = "Invalid password reset token.";
        String actualTokenError = Constant.WEBDRIVER.findElement(By.xpath("//label[@for='resetToken' and contains(@class, 'validation-error')]")).getText().trim();
        Assert.assertEquals(actualTokenError, expectedTokenError, "Token error message is not displayed as expected");
    }

    @Test
    public void TC13() throws InterruptedException {
        System.out.println("TC13: Show error when password and confirm password do not match");

        String registeredEmail = "hongduyen161005@gmail.com";

        HomePage homePage = new HomePage();

        homePage.open();
        homePage.gotoLoginPage();

        Constant.WEBDRIVER.findElement(By.xpath("//a[@href='/Account/ForgotPassword.cshtml']")).click();
        Thread.sleep(2000);

        Constant.WEBDRIVER.findElement(By.id("email")).sendKeys(registeredEmail);

        WebElement sendBtn = Constant.WEBDRIVER.findElement(By.xpath("//input[@value='Send Instructions']"));
        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].click();", sendBtn);
        Thread.sleep(3000);

        String resetUrl = Constant.RAILWAY_URL.replace("Page/HomePage.cshtml", "Account/PasswordReset.cshtml");
        Constant.WEBDRIVER.navigate().to(resetUrl);
        Thread.sleep(2000);

        Constant.WEBDRIVER.findElement(By.id("newPassword")).sendKeys("NewPassword123!");
        Constant.WEBDRIVER.findElement(By.id("confirmPassword")).sendKeys("KhacBietHoanToan456@");

        WebElement resetBtn = Constant.WEBDRIVER.findElement(By.xpath("//input[@value='Reset Password']"));
        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].click();", resetBtn);
        Thread.sleep(2000);

        String expectedFormError = "Could not reset password. Please correct the errors and try again.";
        String actualFormError = Constant.WEBDRIVER.findElement(By.xpath("//p[contains(@class, 'message error')]")).getText().trim();
        Assert.assertEquals(actualFormError, expectedFormError, "Form error message is not displayed as expected");

        String expectedConfirmPassError = "The password confirmation did not match the new password.";
        String actualConfirmPassError = Constant.WEBDRIVER.findElement(By.xpath("//label[@for='confirmPassword' and contains(@class, 'validation-error')]")).getText().trim();
        Assert.assertEquals(actualConfirmPassError, expectedConfirmPassError, "Confirm Password error message is not displayed as expected");
    }
}
