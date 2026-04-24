package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import common.Constant;
import common.Utilities;

public class ChangePasswordPage extends GeneralPage {
    private final By txtCurrentPassword = By.xpath("//input[@id='currentPassword']");
    private final By txtNewPassword = By.xpath("//input[@id='newPassword']");
    private final By txtConfirmPassword = By.xpath("//input[@id='confirmPassword']");
    private final By btnChangePassword = By.xpath("//input[@value='Change Password']");
    private final By lblSuccessMessage = By.xpath("//p[@class='message success']");

    public WebElement getTxtCurrentPassword() {
        return Constant.WEBDRIVER.findElement(txtCurrentPassword);
    }

    public WebElement getTxtNewPassword() {
        return Constant.WEBDRIVER.findElement(txtNewPassword);
    }

    public WebElement getTxtConfirmPassword() {
        return Constant.WEBDRIVER.findElement(txtConfirmPassword);
    }

    public WebElement getBtnChangePassword() {
        return Constant.WEBDRIVER.findElement(btnChangePassword);
    }

    public WebElement getLblSuccessMessage() {
        return Constant.WEBDRIVER.findElement(lblSuccessMessage);
    }

    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        Utilities.sleep(1200);
        this.getTxtCurrentPassword().sendKeys(currentPassword);
        Utilities.sleep(1200);
        this.getTxtNewPassword().sendKeys(newPassword);
        Utilities.sleep(1200);
        this.getTxtConfirmPassword().sendKeys(confirmPassword);
        Utilities.sleep(1200);
        this.getBtnChangePassword().click();
        Utilities.sleep(2500);
    }
}
