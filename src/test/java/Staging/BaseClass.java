package Staging;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseClass {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeSuite
    public void startDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 20);
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
