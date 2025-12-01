package Staging;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class SurveySend extends BaseClass {

    String dobToEnter = BaseClass.P_DOB;
    Random ran = new Random();

    // JS click helper
    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Wait for overlay/modals to disappear
    private void waitForOverlayToDisappear() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-background")));
        } catch (TimeoutException e) {
            // overlay not present
        }
    }

    @Test
    public void AddToCart() throws InterruptedException {
        waitForOverlayToDisappear();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='main-contents']/div/div/div/div/div[5]/div[1]/span[1]/div[2]")));
        jsClick(addBtn);
        Thread.sleep(1000);

        WebElement newAssessmentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[4]/div[3]/div[1]")));
        jsClick(newAssessmentBtn);
        Thread.sleep(1000);

        WebElement cptInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[1]/div/div[2]/div/span[1]/input")));
        cptInput.sendKeys("Anxiety");
        Thread.sleep(500);

        WebElement assessmentOpen = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[1]/div/div[5]/div[2]/div/div[1]/div/div[1]/h3")));
        jsClick(assessmentOpen);
        Thread.sleep(1000);

        WebElement cptSelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[1]/div/div[5]/div[2]/div/div[1]/div/div[2]/div/div/div[2]/div/div/div[1]/div[2]/div[3]/span")));
        jsClick(cptSelect);
        Thread.sleep(500);
    }

    @Test(dependsOnMethods = "AddToCart")
    public void SendAssessment() throws InterruptedException {
        waitForOverlayToDisappear();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement radiobtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/div/div[4]/div[1]")));
        jsClick(radiobtn);
        Thread.sleep(500);

        WebElement sendNow = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[4]/div/div[2]/div/div/div[1]/div/div/div[5]/span/span")));
        jsClick(sendNow);
        System.out.println("Assessment sent to 'This Device'");
        Thread.sleep(1000);
    }

    @Test(dependsOnMethods = "SendAssessment")
    public void DOB() throws InterruptedException {
        waitForOverlayToDisappear();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String[] parts = dobToEnter.split("/");
        String month = String.valueOf(Integer.parseInt(parts[0].trim()));
        String day = String.valueOf(Integer.parseInt(parts[1].trim()));
        String year = parts[2].trim();

        WebElement monthDrop = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='month']")));
        monthDrop.click();
        monthDrop.findElement(By.xpath(".//option[@value='" + month + "']")).click();

        WebElement dayDrop = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='day']")));
        dayDrop.click();
        dayDrop.findElement(By.xpath(".//option[@value='" + day + "']")).click();

        WebElement yearDrop = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='year']")));
        yearDrop.click();
        yearDrop.findElement(By.xpath(".//option[@value='" + year + "']")).click();

        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-section\"]/div/div[1]/div/div[2]/button/span")));
        jsClick(continueBtn);
        Thread.sleep(500);

        WebElement agreeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-section\"]/div/div[2]/div/div[4]/button")));
        jsClick(agreeBtn);
        Thread.sleep(500);

        // Random answers loop
        while (true) {
            try {
                if (driver.findElements(By.xpath("//div[@class='results-title' and contains(.,'Scorecard')]")).size() > 0) {
                    System.out.println("Survey Completed!");
                    break;
                }

                List<WebElement> answers = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//label[contains(@class,'selected__label')]")));

                WebElement randomAnswer = answers.get(ran.nextInt(answers.size()));
                jsClick(randomAnswer);

                Thread.sleep(700);

            } catch (Exception e) {
                Thread.sleep(500);
            }
        }
    }
}
