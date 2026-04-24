package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import common.Constant;
import common.Utilities;

public class RegisterPage extends GeneralPage {
    private final By txtEmail = By.xpath("//input[@id='email']");
    private final By txtPassword = By.xpath("//input[@id='password']");
    private final By txtConfirmPassword = By.xpath("//input[@id='confirmPassword']");
    private final By txtPid = By.xpath("//input[@id='pid']");
    private final By btnRegister = By.xpath("//input[@value='Register']");
    private final By lblRegisterSuccessMsg = By.xpath("//div[@id='content']//p");
    private final By lblRegisterErrorMsg = By.xpath("//p[contains(@class, 'message error')]");

    public WebElement getTxtEmail() {
        return Constant.WEBDRIVER.findElement(txtEmail);
    }

    public WebElement getTxtPassword() {
        return Constant.WEBDRIVER.findElement(txtPassword);
    }

    public WebElement getTxtConfirmPassword() {
        return Constant.WEBDRIVER.findElement(txtConfirmPassword);
    }

    public WebElement getTxtPid() {
        return Constant.WEBDRIVER.findElement(txtPid);
    }

    public WebElement getBtnRegister() {
        return Constant.WEBDRIVER.findElement(btnRegister);
    }

    public WebElement getLblRegisterSuccessMsg() {
        return Constant.WEBDRIVER.findElement(lblRegisterSuccessMsg);
    }

    public WebElement getLblRegisterErrorMsg() {
        return Constant.WEBDRIVER.findElement(lblRegisterErrorMsg);
    }

    public void register(String email, String password, String confirmPassword, String pid) {
        Utilities.sleep(1200);
        this.getTxtEmail().sendKeys(email);
        Utilities.sleep(1200);
        this.getTxtPassword().sendKeys(password);
        Utilities.sleep(1200);
        this.getTxtConfirmPassword().sendKeys(confirmPassword);
        Utilities.sleep(1200);
        this.getTxtPid().sendKeys(pid);
        Utilities.sleep(1200);
        this.getBtnRegister().click();
        Utilities.sleep(2500);
    }
}
