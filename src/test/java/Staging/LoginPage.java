package Staging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import java.time.Duration;

public class LoginPage {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @Test
    public void loginTest() {

        ChromeOptions options = new ChromeOptions();

        // 🔥 FIXED HEADLESS for Jenkins
        options.addArguments("--headless=chrome");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Smooth stability options
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-site-isolation-trials");

        // DRIVER INIT
        driver = WebDriverSetup.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(120));

        // OPEN URL
        driver.get("https://stagingportal.outcomemd.com/");

        // PRINT PAGE SOURCE if login page fails (Debug)
        System.out.println("PAGE TITLE: " + driver.getTitle());

        // LOGIN FLOW
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")))
                .sendKeys("nbhatkar@outcomemd.com");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']")))
                .sendKeys("Staging@1234");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sign In']")))
                .click();

        System.out.println("Login successful");
    }
}
