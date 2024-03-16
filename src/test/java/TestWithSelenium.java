import io.qameta.allure.Attachment;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestWithSelenium {

    @Test
    @Issue("MYPROJECT-1")
    public void googleTitleTest() throws IOException {
        WebDriver driver = setupDriver();
        navigateToGoogle(driver);
        takeScreenshot(driver);
        verifyTitle(driver);
        driver.quit();
    }

    @Step("Setup WebDriver")
    private WebDriver setupDriver() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        return new RemoteWebDriver(new URL("http://192.168.8.110:4444"), capabilities);
    }

    @Step("Navigate to Google")
    private void navigateToGoogle(WebDriver driver) {
        driver.get("https://www.google.com");
    }

    @Step("Take a screenshot")
    private void takeScreenshot(WebDriver driver) throws IOException {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("src/test/resources/screenshots/googleTitleTest.png"));
        // Attach screenshot to Allure report
        attachScreenshotToAllure(driver);
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private byte[] attachScreenshotToAllure(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Step("Verify page title")
    private void verifyTitle(WebDriver driver) {
        Assert.assertEquals(driver.getTitle(), "Google", "Page title is not as expected");
    }
}
