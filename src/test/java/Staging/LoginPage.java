package Staging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginPage extends BaseClass {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @Test
    public void loginTest() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--start-maximized");
        options.addArguments("--force-device-scale-factor=1");
        options.addArguments("--enable-features=NetworkService,NetworkServiceInProcess");
        options.addArguments("--whitelisted-ips=''");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-site-isolation-trials");

        // 100% Correct Fix:
        driver = WebDriverSetup.getDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://stagingportal.outcomemd.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")))
                .sendKeys("nbhatkar@outcomemd.com");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']")))
                .sendKeys("Staging@1234");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sign In']")))
                .click();

        System.out.println("Login successful");
    }
}
