package pageobjects;

import common.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookTicketPage extends GeneralPage {

    private final By ddlDepartDate = By.name("Date");
    private final By ddlDepartStation = By.name("DepartStation");
    private final By ddlArriveStation = By.name("ArriveStation");
    private final By ddlSeatType = By.name("SeatType");
    private final By ddlTicketAmount = By.name("TicketAmount");
    private final By btnBookTicket = By.xpath("//input[@value='Book ticket']");
    private final By lblBookSuccessMsg = By.xpath("//h1[normalize-space()='Ticket booked successfully!']");

    private void clickElement(By locator) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(10));
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

    public WebElement getDdlDepartDate() {
        return waitForElement(ddlDepartDate);
    }

    public WebElement getDdlDepartStation() {
        return waitForElement(ddlDepartStation);
    }

    public WebElement getDdlArriveStation() {
        return waitForElement(ddlArriveStation);
    }

    public WebElement getDdlSeatType() {
        return waitForElement(ddlSeatType);
    }

    public WebElement getDdlTicketAmount() {
        return waitForElement(ddlTicketAmount);
    }

    public WebElement getBtnBookTicket() {
        return waitForElement(btnBookTicket);
    }

    public WebElement getLblBookSuccessMsg() {
        return waitForElement(lblBookSuccessMsg);
    }

    public boolean isBookSuccess() {
        try {
            return Constant.WEBDRIVER.findElement(lblBookSuccessMsg).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasOption(WebElement dropdown, String expectedText) {
        Select select = new Select(dropdown);
        for (WebElement option : select.getOptions()) {
            String actual = option.getText().trim();
            if (actual.equalsIgnoreCase(expectedText.trim())) {
                return true;
            }
        }
        return false;
    }

    public void bookTicket(String departDate, String departStation, String arriveStation, String seatType, String amount) {
        int maxAttempts = 30;

        for (int attempt = 1; attempt <= 30; attempt++) {
            try {
                Constant.WEBDRIVER.navigate().to("http://railwayb1.somee.com/Page/BookTicketPage.cshtml");
                waitForElement(By.name("Date"));

                new Select(getDdlDepartDate()).selectByVisibleText(departDate);

                if (!hasOption(getDdlDepartStation(), departStation)) {
                    throw new RuntimeException("Depart station not found: " + departStation);
                }
                new Select(getDdlDepartStation()).selectByVisibleText(departStation);

                if (!hasOption(getDdlArriveStation(), arriveStation)) {
                    throw new RuntimeException("Arrive station not found: " + arriveStation);
                }
                new Select(getDdlArriveStation()).selectByVisibleText(arriveStation);

                new Select(getDdlSeatType()).selectByVisibleText(seatType);
                new Select(getDdlTicketAmount()).selectByVisibleText(amount);

                clickElement(btnBookTicket);

                if (isBookSuccess()) {
                    return;
                }

                System.out.println("Fixed book attempt " + attempt + " failed. Retry...");
            } catch (Exception e) {
                System.out.println("Fixed book attempt " + attempt + " error: " + e.getMessage());
            }
        }

        throw new RuntimeException("Không thể book vé cố định sau " + maxAttempts + " lần thử.");
    }

    public String[] bookTicketWithDynamicStations(String departDate, String seatType, String amount) {
        int maxAttempts = 20;
        Random random = new Random();

        for (int attempt = 1; attempt <= 20; attempt++) {
            try {
                Constant.WEBDRIVER.navigate().to("http://railwayb1.somee.com/Page/BookTicketPage.cshtml");
                waitForElement(By.name("Date"));

                new Select(getDdlDepartDate()).selectByVisibleText(departDate);

                Select departSelect = new Select(getDdlDepartStation());
                List<WebElement> departOptions = departSelect.getOptions();

                List<Integer> validDepartIndexes = new ArrayList<>();
                for (int i = 0; i < departOptions.size(); i++) {
                    String text = departOptions.get(i).getText().trim();
                    if (!text.isEmpty() && !text.equalsIgnoreCase("select")) {
                        validDepartIndexes.add(i);
                    }
                }

                if (validDepartIndexes.isEmpty()) {
                    throw new RuntimeException("Không có ga đi hợp lệ.");
                }

                int chosenDepartIndex = -1;
                int chosenArriveIndex = -1;

                for (Integer departIndex : validDepartIndexes) {
                    new Select(getDdlDepartDate()).selectByVisibleText(departDate);

                    departSelect = new Select(getDdlDepartStation());
                    departSelect.selectByIndex(departIndex);
                    String departStation = departSelect.getFirstSelectedOption().getText().trim();

                    Select arriveSelect = new Select(getDdlArriveStation());
                    List<WebElement> arriveOptions = arriveSelect.getOptions();

                    List<Integer> validArriveIndexes = new ArrayList<>();
                    for (int i = 0; i < arriveOptions.size(); i++) {
                        String text = arriveOptions.get(i).getText().trim();
                        if (!text.isEmpty()
                                && !text.equalsIgnoreCase("select")
                                && !text.equalsIgnoreCase(departStation)) {
                            validArriveIndexes.add(i);
                        }
                    }

                    if (!validArriveIndexes.isEmpty()) {
                        chosenDepartIndex = departIndex;
                        chosenArriveIndex = validArriveIndexes.get(random.nextInt(validArriveIndexes.size()));
                        break;
                    }
                }

                if (chosenDepartIndex == -1 || chosenArriveIndex == -1) {
                    throw new RuntimeException("Không tìm được cặp ga đi/ga đến hợp lệ.");
                }

                new Select(getDdlDepartDate()).selectByVisibleText(departDate);

                Select finalDepartSelect = new Select(getDdlDepartStation());
                finalDepartSelect.selectByIndex(chosenDepartIndex);
                String finalDepartStation = finalDepartSelect.getFirstSelectedOption().getText().trim();

                Select finalArriveSelect = new Select(getDdlArriveStation());
                finalArriveSelect.selectByIndex(chosenArriveIndex);
                String finalArriveStation = finalArriveSelect.getFirstSelectedOption().getText().trim();

                new Select(getDdlSeatType()).selectByVisibleText(seatType);
                new Select(getDdlTicketAmount()).selectByVisibleText(amount);

                clickElement(btnBookTicket);

                if (isBookSuccess()) {
                    return new String[]{finalDepartStation, finalArriveStation};
                }

                System.out.println("Dynamic book attempt " + attempt + " failed. Retry...");
            } catch (Exception e) {
                System.out.println("Dynamic book attempt " + attempt + " error: " + e.getMessage());
            }
        }

        throw new RuntimeException("Không thể book vé động sau " + maxAttempts + " lần thử.");
    }

    public String getTicketDepartStation() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[1]")
        ).getText().trim();
    }

    public String getTicketArriveStation() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[2]")
        ).getText().trim();
    }

    public String getTicketSeatType() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[3]")
        ).getText().trim();
    }

    public String getTicketDepartDate() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[4]")
        ).getText().trim();
    }

    public String getTicketBookDate() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[5]")
        ).getText().trim();
    }

    public String getTicketExpiredDate() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[6]")
        ).getText().trim();
    }

    public String getTicketAmount() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[7]")
        ).getText().trim();
    }

    public String getTicketTotalPrice() {
        return waitForElement(
                By.xpath("//table[contains(@class,'MyTable')]//tr[2]/td[8]")
        ).getText().trim();
    }
}