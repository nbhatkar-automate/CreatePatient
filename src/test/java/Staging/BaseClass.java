package Staging;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import java.time.Duration;

public class BaseClass {

    public static WebDriver driver;
    public static WebDriverWait wait;
    public static String P_DOB; // For Global DOB Use

    @BeforeSuite
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = WebDriverSetup.getDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        System.out.println("===== DRIVER STARTED =====");
    }

    @AfterSuite
    public void tearDown() {
        System.out.println("===== DRIVER CLOSED =====");
        // Do NOT quit driver here in Jenkins
    }
}
