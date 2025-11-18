package Staging;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverSetup {

    private static WebDriver driver;

    public static WebDriver getDriver(ChromeOptions options) {

        if (driver == null) {
            driver = new ChromeDriver(options);
        }

        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
