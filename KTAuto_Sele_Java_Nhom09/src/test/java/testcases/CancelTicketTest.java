package testcases;

import common.Constant;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.BookTicketPage;
import pageobjects.GeneralPage;
import pageobjects.HomePage;
import pageobjects.LoginPage;
import pageobjects.MyTicketPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CancelTicketTest {

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
    public void TC16() {
        System.out.println("TC16 - User can cancel a ticket");

        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();
        GeneralPage generalPage = loginPage.login("trangnguyen12345@gmail.com", "trang12345");

        BookTicketPage bookTicketPage = generalPage.gotoBookTicketPage();

        Select selectDate = new Select(bookTicketPage.getDdlDepartDate());
        String departDate = selectDate.getOptions().get(3).getText().trim();

        String seatType = "Soft bed with air conditioner";
        String amount = "1";

        String[] stations = bookTicketPage.bookTicketWithDynamicStations(departDate, seatType, amount);
        String departStation = stations[0];
        String arriveStation = stations[1];

        System.out.println("Depart station used: " + departStation);
        System.out.println("Arrive station used: " + arriveStation);

        MyTicketPage myTicketPage = generalPage.gotoMyTicketPage();

        // Lấy ticketId của vé vừa mới đặt để tránh xóa nhầm các vé trùng lặp khác
        String onclickText = myTicketPage.getCancelTicketUrlOfNewestTicket(departDate, departStation, arriveStation, seatType, amount);
        String ticketId = "";
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(onclickText);
        if (matcher.find()) {
            ticketId = matcher.group();
        }

        System.out.println("Canceling ticket with ID: " + ticketId);

        Assert.assertFalse(ticketId.isEmpty(), "Could not find ticket ID to cancel.");

        myTicketPage.cancelTicketById(ticketId);
        myTicketPage.acceptCancelAlert();
        myTicketPage.waitUntilTicketDisappearById(ticketId);

        Assert.assertFalse(
                myTicketPage.isTicketDisplayedById(ticketId),
                "The canceled ticket with ID " + ticketId + " is still displayed."
        );
    }
}