package Staging;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class SurveySend extends BaseClass {
String dobToEnter = BaseClass.globalDOB;


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
        
        @Test(dependsOnMethods = "SendAssessment")
        public void DOB() throws InterruptedException {

            String dob = BaseClass.globalDOB;   // "MM/DD/YYYY"
            String[] parts = dob.split("/");

         // Remove leading 0 → e.g. 01 -> 1 for survey page and trim white spaces
            String month = String.valueOf(Integer.parseInt(parts[0].trim()));
            String day   = String.valueOf(Integer.parseInt(parts[1].trim()));
            String year  = parts[2].trim();
            
            // MONTH Dropdown
            WebElement monthDrop = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='month']")));
            monthDrop.click();
            monthDrop.findElement(By.xpath(".//option[@value='" + month + "']")).click();

            // DAY Dropdown
            WebElement dayDrop = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='day']")));
            dayDrop.click();
            dayDrop.findElement(By.xpath(".//option[@value='" + day + "']")).click();

            // YEAR Dropdown
            WebElement yearDrop = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='year']")));
            yearDrop.click();
            yearDrop.findElement(By.xpath(".//option[@value='" + year + "']")).click();

            // CONTINUE button
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"main-section\"]/div/div[1]/div/div[2]/button/span"))).click();

            System.out.println("DOB Verified. Proceeding futher");
            
            // Agree Button click
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-section\"]/div/div[2]/div/div[4]/button"))).click();
            
           // Selecting Answers
            Random ran = new Random();

            while (true) {
                try {
                    // FINAL PAGE DETECTION
                    if (driver.findElements(By.xpath("//div[@class='results-title' and contains(.,'Scorecard')]")).size() > 0) {
                        System.out.println("Survey Completed!");
                        break;
                    }

                    // Wait for answer options
                    List<WebElement> answers = wait.until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//label[contains(@class,'selected__label')]")));

                    // Random answer click
                    WebElement randomAnswer = answers.get(ran.nextInt(answers.size()));
                    randomAnswer.click();

                    Thread.sleep(700);

                } catch (Exception e) {
                    Thread.sleep(500);
                }
            }
        }
 }
