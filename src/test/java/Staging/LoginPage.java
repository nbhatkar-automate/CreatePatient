package Staging;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class LoginPage extends BaseClass {

    @Test
    public void loginTest() {

        // Use driver & wait from BaseClass
        driver = BaseClass.driver;
        wait = BaseClass.wait;

        driver.get("https://stagingportal.outcomemd.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")))
                .sendKeys("nbhatkar@outcomemd.com");//*[@id="app-component"]/div[3]/div/div/div[2]/div[2]/div[3]/div[1]/span/input

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']")))
                .sendKeys("Staging@1234");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sign In']")))
                .click();

        System.out.println("Login successful");
    }
}
