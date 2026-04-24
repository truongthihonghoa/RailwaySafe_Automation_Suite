package common;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class TestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String reportFolder = Utilities.getProjectPath() + File.separator + "test-output";
        File dir = new File(reportFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String path = reportFolder + File.separator + "ExtentReport.html";
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(path);

        htmlReporter.config().setDocumentTitle("Automation Test Report");
        htmlReporter.config().setReportName("Railway Test Results");
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Project", "KTAuto_Sele_Java_Nhom09");
        extent.setSystemInfo("Author", "Nhom 09");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description != null && !description.isEmpty()) {
            testName = testName + " - " + description;
        }
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Ghi lại lỗi log text
        extentTest.get().log(Status.FAIL, "Test Failed");
        extentTest.get().log(Status.FAIL, result.getThrowable());

        if (Constant.WEBDRIVER != null) {
            try {
                // 1. Chụp và lưu ảnh thành file .png vật lý vào máy tính theo tên Test Case để bạn có thể xem trong thư mục
                String localScreenshotPath = Utilities.takeScreenshot(result.getMethod().getMethodName());

                // 2. Chụp một bản Base64 thứ 2 để nhúng thẳng vào HTML
                String base64Screenshot = Utilities.takeScreenshotBase64();
                if (base64Screenshot != null) {String imageHTML = "<img src=\"data:image/png;base64," + base64Screenshot + "\" width=\"100%\" />";
                    extentTest.get().log(Status.FAIL, "Ảnh chụp màn hình lỗi (Ảnh gốc cũng được lưu tại: " + localScreenshotPath + "): " + imageHTML);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi thêm ảnh vào report: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test Skipped");
        extentTest.get().log(Status.SKIP, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}