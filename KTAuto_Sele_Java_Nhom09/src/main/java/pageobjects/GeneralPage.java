package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import common.Constant;
import common.Utilities;
import pageobjects.BookTicketPage;
import org.openqa.selenium.JavascriptExecutor;
public class GeneralPage {
    // Locators
    private final By lblWelcomeMessage = By.xpath("//*[contains(@class,'account')]");
    private final By tabLogin = By.xpath("//a[normalize-space()='Login']");
    private final By tabRegister = By.xpath("//a[normalize-space()='Register']");
    private final By tabBookTicket = By.xpath("//a[normalize-space()='Book ticket']");
    private final By tabMyTicket = By.xpath("//a[normalize-space()='My ticket']");
    private final By tabChangePassword = By.xpath("//a[normalize-space()='Change password']");
    private final By tabLogout = By.xpath("//a[normalize-space()='Log out']");
    private final By lblHeader = By.xpath("//div[@id='content']//h1");

    protected WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement getLblWelcomeMessage() {
        return waitForElement(lblWelcomeMessage);
    }

    protected WebElement getTabLogin() {
        return waitForElement(tabLogin);
    }

    protected WebElement getTabRegister() {
        return waitForElement(tabRegister);
    }

    protected WebElement getTabBookTicket() {
        return waitForElement(tabBookTicket);
    }

    public WebElement getTabMyTicket() {
        return waitForElement(tabMyTicket);
    }

    public WebElement getTabChangePassword() {
        return waitForElement(tabChangePassword);
    }

    public WebElement getTabLogout() {
        return waitForElement(tabLogout);
    }

    protected WebElement getLblHeader() {
        return waitForElement(lblHeader);
    }

    public String getWelcomeMessage() {
        return this.getLblWelcomeMessage().getText();
    }

    public boolean isLogoutTabDisplayed() {
        return !Constant.WEBDRIVER.findElements(tabLogout).isEmpty();
    }

    public LoginPage gotoLoginPage() {
        this.getTabLogin().click();
        return new LoginPage();
    }

    public RegisterPage gotoRegisterPage() {
        this.getTabRegister().click();
        return new RegisterPage();
    }

    public ChangePasswordPage gotoChangePasswordPage() {
        this.getTabChangePassword().click();
        return new ChangePasswordPage();
    }

    public BookTicketPage gotoBookTicketPage() {
        waitForElement(By.linkText("Book ticket")).click();
        return new BookTicketPage();
    }
    public MyTicketPage gotoMyTicketPage() {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));
        WebElement myTicketTab = wait.until(ExpectedConditions.presenceOfElementLocated(tabMyTicket));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(tabMyTicket));
            myTicketTab.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) Constant.WEBDRIVER)
                    .executeScript("arguments[0].click();", myTicketTab);
        }

        return new MyTicketPage();
    }

}