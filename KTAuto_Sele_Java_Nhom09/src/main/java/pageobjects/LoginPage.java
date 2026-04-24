package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import common.Constant;
import common.Utilities;

public class LoginPage extends GeneralPage {
    private final By txtUsername = By.xpath("//input[@id='username']");
    private final By txtPassword = By.xpath("//input[@id='password']");
    private final By btnLogin = By.xpath("//input[@title='Login']");
    private final By lblLoginErrorMsg = By.xpath("//p[@class='message error LoginForm']");

    public WebElement getTxtUsername() {
        return Constant.WEBDRIVER.findElement(txtUsername);
    }

    public WebElement getTxtPassword() {
        return Constant.WEBDRIVER.findElement(txtPassword);
    }

    public WebElement getBtnLogin() {
        return Constant.WEBDRIVER.findElement(btnLogin);
    }

    public WebElement getLblLoginErrorMsg() {
        return Constant.WEBDRIVER.findElement(lblLoginErrorMsg);
    }

    public GeneralPage login(String username, String password) {
        Utilities.sleep(1200);
        this.getTxtUsername().sendKeys(username);
        Utilities.sleep(1200);
        this.getTxtPassword().sendKeys(password);
        Utilities.sleep(1200);
        this.getBtnLogin().click();
        Utilities.sleep(2500);

        return new GeneralPage();
    }
}
