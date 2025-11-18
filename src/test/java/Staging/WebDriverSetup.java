package Staging;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverSetup {

    public static WebDriver getDriver() {

        ChromeOptions options = new ChromeOptions();

        // Important for Jenkins/Linux:
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        // Disable automation banners
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Disable infobars
        options.addArguments("disable-infobars");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }
}
