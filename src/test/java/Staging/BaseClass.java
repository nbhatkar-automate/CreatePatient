package Staging;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class BaseClass {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeSuite
    public void startDriver() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().window().maximize();
        System.out.println("===== DRIVER STARTED (BeforeSuite) =====");
    }

    @AfterSuite
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
            System.out.println("===== DRIVER CLOSED (AfterSuite) =====");
        }
    }
}
