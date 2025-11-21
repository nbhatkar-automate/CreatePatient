package Staging;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverSetup {

    public static WebDriver getDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }
}
