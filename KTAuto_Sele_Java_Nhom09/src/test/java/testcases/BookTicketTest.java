package testcases;

import common.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.BookTicketPage;
import pageobjects.HomePage;
import pageobjects.LoginPage;
import pageobjects.GeneralPage;

public class BookTicketTest {
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
    public void TC14() {
        System.out.println("TC14 - User can book 1 ticket at a time");

        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();
        GeneralPage generalPage = loginPage.login("phamthicamtien12345@gmail.com", "Tien_21092005");

        BookTicketPage bookTicketPage = homePage.gotoBookTicketPage();

        String expectedDepartStation = "Sài Gòn";
        String expectedArriveStation = "Nha Trang";
        String expectedSeatType = "Soft bed with air conditioner";
        String expectedAmount = "1";


        Select selectDate = new Select(bookTicketPage.getDdlDepartDate());
        int size = selectDate.getOptions().size();
        int randomIndex = new java.util.Random().nextInt(size);

        String expectedDepartDate = selectDate.getOptions().get(randomIndex).getText();

        bookTicketPage.bookTicket(expectedDepartDate, expectedDepartStation,
                expectedArriveStation, expectedSeatType, expectedAmount);

        String actualSuccessMsg = bookTicketPage.getLblBookSuccessMsg().getText().trim();
        String expectedSuccessMsg = "Ticket booked successfully!";

        Assert.assertTrue(actualSuccessMsg.equalsIgnoreCase(expectedSuccessMsg),
                "Success message is not displayed correctly. Actual: " + actualSuccessMsg);

        Assert.assertEquals(bookTicketPage.getTicketDepartDate(), expectedDepartDate);
        Assert.assertFalse(bookTicketPage.getTicketDepartStation().isEmpty());
        Assert.assertEquals(bookTicketPage.getTicketArriveStation(), expectedArriveStation);
        Assert.assertEquals(bookTicketPage.getTicketSeatType(), expectedSeatType);
        Assert.assertEquals(bookTicketPage.getTicketAmount(), expectedAmount);
    }

    @Test
    public void TC15() throws InterruptedException {
        System.out.println("TC15 - User can open Book ticket page by clicking on Book ticket link in Train timetable page");
        HomePage homePage = new HomePage();
        homePage.open();
        LoginPage loginPage = homePage.gotoLoginPage();
        GeneralPage generalPage = loginPage.login("hongduyen161005@gmail.com", "hongduyen161005Tmp1!");
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(15L));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='menu']//span[text()='Timetable']")));
        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//span[text()='Timetable']")).click();
        String xpathBookTicket = "//tr[td[contains(., 'Huế')]/following-sibling::td[contains(., 'Sài Gòn')]]//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'book')]";
        WebElement bookTicketLink = null;


        while (bookTicketLink == null) {
            try {
                bookTicketLink = Constant.WEBDRIVER.findElement(By.xpath(xpathBookTicket));
            } catch (Exception var13) {
                Constant.WEBDRIVER.navigate().refresh();
                Thread.sleep(2500L);
            }
        }


        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].scrollIntoView(true);", new Object[]{bookTicketLink});
        ((JavascriptExecutor) Constant.WEBDRIVER).executeScript("arguments[0].click();", new Object[]{bookTicketLink});
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@id='content']/h1"), "Book ticket"));
        wait.until((driver) -> {
            Select depart = new Select(driver.findElement(By.name("DepartStation")));
            return depart.getFirstSelectedOption().getText().trim().contains("Huế");
        });
        wait.until((driver) -> {
            Select arrive = new Select(driver.findElement(By.name("ArriveStation")));
            return arrive.getFirstSelectedOption().getText().trim().contains("Sài Gòn");
        });
        String actualTitle = Constant.WEBDRIVER.findElement(By.xpath("//div[@id='content']/h1")).getText().trim();
        Assert.assertEquals(actualTitle, "Book ticket", "Book ticket page is not loaded.");
        Select departFromSelect = new Select(Constant.WEBDRIVER.findElement(By.name("DepartStation")));
        Select arriveAtSelect = new Select(Constant.WEBDRIVER.findElement(By.name("ArriveStation")));
        String selectedDepart = departFromSelect.getFirstSelectedOption().getText().trim();
        String selectedArrive = arriveAtSelect.getFirstSelectedOption().getText().trim();
        boolean isDepartCorrect = selectedDepart.contains("Huế");
        boolean isArriveCorrect = selectedArrive.contains("Sài Gòn");
        Assert.assertTrue(isDepartCorrect, "Depart from value is incorrect. Actual: " + selectedDepart);
        Assert.assertTrue(isArriveCorrect, "Arrive at value is incorrect. Actual: " + selectedArrive);
    }
}
