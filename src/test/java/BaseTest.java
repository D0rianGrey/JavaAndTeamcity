import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.URL;

public class BaseTest {

    WebDriver driver = null;

    @BeforeTest
    protected void setUp() throws IOException {
        driver = setupDriver();
    }


    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Step("Setup WebDriver")
    protected WebDriver setupDriver() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        return new RemoteWebDriver(new URL("http://192.168.8.106:4444"), capabilities);
    }
}
