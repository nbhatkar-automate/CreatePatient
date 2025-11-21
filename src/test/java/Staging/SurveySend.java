package Staging;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class SurveySend extends BaseClass {

    @Test
    public void AddToCart() throws InterruptedException {

        // driver & wait already available from BaseClass (BeforeSuite)
        
        WebElement addBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id='main-contents']/div/div/div/div/div[5]/div[1]/span[1]/div[2]")));

        // Scroll + JS Click
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addBtn);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

        // click on new assessment button
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[4]/div[3]/div[1]"))).click();
        Thread.sleep(2000);

        // enter cpt name in textbox
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[1]/div/div[2]/div/span[1]/input")))
                .sendKeys("Anxiety");

        // open assessment
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[1]/div/div[5]/div[2]/div/div[1]/div/div[1]/h3")))
                .click();
        Thread.sleep(2000);

        // add to cart
        WebElement cptselect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[1]/div/div[5]/div[2]/div/div[1]/div/div[2]/div/div/div[2]/div/div/div[1]/div[2]/div[3]/span")));

        // Scroll + JS Click
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", cptselect);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cptselect);
    }

    @Test(dependsOnMethods = "AddToCart")
    public void SendAssessment() throws InterruptedException {

        // Radio Button select - This Device
        WebElement radiobtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/div/div[4]/div[1]")));

        // Scroll + JS Click
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", radiobtn);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radiobtn);

        // Send Now Button
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/div/div[5]/span/span")))
                .click();

        System.out.println("Assessment sent to 'This Device'");
    }
}
