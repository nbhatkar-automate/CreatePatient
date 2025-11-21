package Staging;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class LoginPage extends BaseClass {

    @Test
    public void loginTest() {

        // Driver & wait already initialized in BaseClass
        driver.get("https://stagingportal.outcomemd.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Email']")))
                .sendKeys("nbhatkar@outcomemd.com");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[type='password']")))
                .sendKeys("Staging@1234");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Sign In']")))
                .click();

        System.out.println("Login successful");
    }
}
