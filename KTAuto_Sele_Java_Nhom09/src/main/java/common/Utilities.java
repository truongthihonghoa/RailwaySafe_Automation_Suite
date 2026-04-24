package common;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Utilities {
    public static String getProjectPath() {
        return Path.of("").toAbsolutePath().normalize().toString();
    }

    public static String generateRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String generateRandomEmail() {
        return "auto_" + generateRandomString() + "@mail.com";
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    // Đổi tên biến screenshotName thành một tên CỐ ĐỊNH theo tên Test Case
    // Khi tên file cố định, Windows sẽ tự động ghi đè (thay thế) file cũ bằng file mới nhất
    public static String takeScreenshot(String testName) {
        // Chỉ dùng tên testName làm tên file, KHÔNG CÓ THỜI GIAN VÀO TÊN FILE NỮA
        String screenshotName = testName + ".png";

        String dirPath = getProjectPath() + File.separator + "test-output" + File.separator + "screenshots";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String screenshotPath = dirPath + File.separator + screenshotName;

        try {
            // Cuộn màn hình một chút để thấy nội dung cần chụp
            JavascriptExecutor js = (JavascriptExecutor) Constant.WEBDRIVER;
            js.executeScript("window.scrollBy(0, 300);");
            Thread.sleep(1000);

            // Chụp và lưu file đè lên file cũ
            File source = ((TakesScreenshot) Constant.WEBDRIVER).getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return screenshotPath;
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
            return null;
        }
    }

    public static String takeScreenshotBase64() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Constant.WEBDRIVER;
            js.executeScript("window.scrollBy(0, 300);");
            Thread.sleep(1000);

            return ((TakesScreenshot) Constant.WEBDRIVER).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot base64: " + e.getMessage());
            return null;
        }
    }
}