package pageobjects;

import common.Constant;
import common.Utilities;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MyTicketPage extends GeneralPage {

    private final WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));

    private By myTicketTable() {
        return By.xpath("//table[@class='MyTable']");
    }

    private By getTicketRow(String departDate, String departStation, String arriveStation, String seatType, String amount) {
        return By.xpath("//table[@class='MyTable']//tr[" +
                "td[2][normalize-space()='" + departStation + "']" +
                " and td[3][normalize-space()='" + arriveStation + "']" +
                " and td[4][normalize-space()='" + seatType + "']" +
                " and td[5][normalize-space()='" + departDate + "']" +
                " and td[9][normalize-space()='" + amount + "']" +
                "]");
    }

    private By getCancelButtonInRow(String departDate, String departStation, String arriveStation, String seatType, String amount) {
        return By.xpath("//table[@class='MyTable']//tr[" +
                "td[2][normalize-space()='" + departStation + "']" +
                " and td[3][normalize-space()='" + arriveStation + "']" +
                " and td[4][normalize-space()='" + seatType + "']" +
                " and td[5][normalize-space()='" + departDate + "']" +
                " and td[9][normalize-space()='" + amount + "']" +
                "]//input[@value='Cancel']");
    }

    private By getTicketRowByIdColumn(String ticketId) {
        return By.xpath("//table[@class='MyTable']//tr[td[1][normalize-space()='" + ticketId + "']]");
    }

    // Match đúng DeleteTicket(9991), không match nhầm 99910 hay 19991
    private By getTicketRowByOnclick(String ticketId) {
        return By.xpath("//table[@class='MyTable']//tr[.//input[@value='Cancel' and contains(@onclick,'DeleteTicket(" + ticketId + ")')]]");
    }

    private By getCancelButtonByIdColumn(String ticketId) {
        return By.xpath("//table[@class='MyTable']//tr[td[1][normalize-space()='" + ticketId + "']]//input[@value='Cancel']");
    }

    // Match đúng nút cancel của ticketId
    private By getCancelButtonByOnclick(String ticketId) {
        return By.xpath("//table[@class='MyTable']//input[@value='Cancel' and contains(@onclick,'DeleteTicket(" + ticketId + ")')]");
    }

    private void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element
        );

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].click();", element);
        }
    }

    public void waitForMyTicketPageLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(myTicketTable()));
    }

    public void cancelTicket(String departDate, String departStation, String arriveStation, String seatType, String amount) {
        clickElement(getCancelButtonInRow(departDate, departStation, arriveStation, seatType, amount));
        Utilities.sleep(1000);
    }

    public boolean isTicketDisplayed(String departDate, String departStation, String arriveStation, String seatType, String amount) {
        try {
            waitForMyTicketPageLoaded();
            WebElement row = Constant.WEBDRIVER.findElement(
                    getTicketRow(departDate, departStation, arriveStation, seatType, amount)
            );
            return row.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isTicketDisplayedById(String ticketId) {
        waitForMyTicketPageLoaded();

        List<WebElement> rowsByIdColumn = Constant.WEBDRIVER.findElements(getTicketRowByIdColumn(ticketId));
        if (!rowsByIdColumn.isEmpty()) {
            return rowsByIdColumn.get(0).isDisplayed();
        }

        List<WebElement> rowsByOnclick = Constant.WEBDRIVER.findElements(getTicketRowByOnclick(ticketId));
        if (!rowsByOnclick.isEmpty()) {
            return rowsByOnclick.get(0).isDisplayed();
        }

        return false;
    }

    public void cancelTicketById(String ticketId) {
        waitForMyTicketPageLoaded();

        if (!Constant.WEBDRIVER.findElements(getCancelButtonByIdColumn(ticketId)).isEmpty()) {
            clickElement(getCancelButtonByIdColumn(ticketId));
        } else if (!Constant.WEBDRIVER.findElements(getCancelButtonByOnclick(ticketId)).isEmpty()) {
            clickElement(getCancelButtonByOnclick(ticketId));
        } else {
            throw new NoSuchElementException("Không tìm thấy ticket với ID: " + ticketId);
        }

        Utilities.sleep(1000);
    }

    public void acceptCancelAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
            throw new RuntimeException("Không thấy alert xác nhận cancel.");
        }
    }

    public void waitUntilTicketDisappearById(String ticketId) {
        By rowById = getTicketRowByIdColumn(ticketId);
        By rowByOnclick = getTicketRowByOnclick(ticketId);

        WebDriverWait shortWait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));

        try {
            shortWait.until(ExpectedConditions.numberOfElementsToBe(rowById, 0));
            return;
        } catch (Exception ignored) {
        }

        shortWait.until(ExpectedConditions.numberOfElementsToBe(rowByOnclick, 0));
    }

    public String getCancelTicketUrlOfNewestTicket(String departDate,
                                                   String departStation,
                                                   String arriveStation,
                                                   String seatType,
                                                   String amount) {
        waitForMyTicketPageLoaded();

        WebElement cancelButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        getCancelButtonInRow(departDate, departStation, arriveStation, seatType, amount)
                )
        );

        String onclick = cancelButton.getAttribute("onclick");

        if (onclick == null || onclick.trim().isEmpty()) {
            throw new RuntimeException("Không lấy được onclick của nút Cancel.");
        }

        return onclick;
    }
    public void waitUntilTicketDisappear(String departDate, String departStation, String arriveStation, String seatType, String amount) {
        By ticketRow = getTicketRow(departDate, departStation, arriveStation, seatType, amount);
        WebDriverWait shortWait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));
        shortWait.until(ExpectedConditions.numberOfElementsToBe(ticketRow, 0));
    }
}