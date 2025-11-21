package Staging;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PatientManagementTab extends BaseClass {

    WebDriver driver;
    WebDriverWait wait;
    String dobFormatted;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @BeforeClass
    public void setup() {

        // Use driver & wait from BaseClass
        this.driver = BaseClass.driver;
        this.wait = BaseClass.wait;

        // Generate random DOB
        int year = 2000 + new java.util.Random().nextInt(25);
        int month = 1 + new java.util.Random().nextInt(12);
        int day = 1 + new java.util.Random().nextInt(28);
        LocalDate dob = LocalDate.of(year, month, day);
        dobFormatted = dob.format(formatter);
    }

    @Test(dependsOnMethods = "Staging.PatientCreate.clickAddButton", priority = 1)
    public void openPatientAndNavigateToManagementTab() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[2]/div/div/div[1]/span[2]/span[2]"))).click();

        WebElement ManagementTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[1]/span[3]")));
        ManagementTab.click();

        System.out.println("Navigated to Management Tab");
    }

    @Test(dependsOnMethods = "openPatientAndNavigateToManagementTab", priority = 2)
    public void addTask() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[1]/div[2]"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/input"))).sendKeys("Filled Via Automation");

        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[6]/div[2]/input")));

        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", dateInput);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[7]/div[2]/div/div[2]/div[1]"))).sendKeys("Automated Comment");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[8]/button"))).click();

        System.out.println("Task Added in Management Tab");
    }

    @Test(dependsOnMethods = "addTask", priority = 3)
    public void addPatientNote() {

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();

        WebElement floatingInput = driver.findElement(By.xpath("//input[contains(@class, 'flatpickr-input')]"));
        wait.until(ExpectedConditions.invisibilityOf(floatingInput));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[2]/div[2]"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div[2]/div/div[2]/div[1]"))).sendKeys("Automated Note");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[4]/button"))).click();

        System.out.println("Note Added in Management Tab");
    }

    @Test(dependsOnMethods = "addPatientNote", priority = 4)
    public void addCallLogEntry() throws InterruptedException {

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
        Thread.sleep(2000);

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-background")));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[3]/div[1]/input")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div")
        ));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[4]/div/div[2]/div[1]")
        )).sendKeys("Automated Entry");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[5]/button")
        )).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div")
        ));

        System.out.println("Call Log Entry Added in Management Tab");
    }

    @Test(dependsOnMethods = "addCallLogEntry", priority = 5)
    public void addMedications() throws InterruptedException {

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[4]/div[2]/div[1]/div/div/div[2]/img")
        )).click();

        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/input")));
        searchBox.sendKeys("Syrup");
        searchBox.click();

        Thread.sleep(3000);

        List<WebElement> suggestions = driver.findElements(By.cssSelector(".selection-list .selection-item"));

        if (suggestions.isEmpty()) {
            throw new RuntimeException("No suggestions found.");
        }

        boolean found = false;

        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().toLowerCase().trim();
            System.out.println("SUGGESTION: " + text);
            if (text.contains("panatuss")) {
                suggestion.click();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("'Syrup' not found in suggestions.");
        }

        WebElement startdate = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[4]/div[2]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", startdate);

        WebElement stopdate = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[5]/div[2]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", stopdate);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div[2]/button"))).click();

        System.out.println("Medication Added in Management Tab");
    }

    @Test(dependsOnMethods = "addMedications", priority = 6)
    public void addDiagnosis() {

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();

        WebElement floatingInput = driver.findElement(By.xpath("//input[contains(@class, 'flatpickr-input')]"));
        wait.until(ExpectedConditions.invisibilityOf(floatingInput));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[5]/div[2]/div[1]/div/div/div[2]"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/span[2]/input"))).sendKeys("test");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/button"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]")));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/ul/li[4]"))).click();
            System.out.println("Diagnosis selected from suggestions.");
        } catch (TimeoutException e) {
            System.out.println("No suggestion found. Attempting to click 'Add New'...");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/ul/li[1]"))).click();
                System.out.println("'Add New' option clicked for diagnosis.");
            } catch (TimeoutException ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        System.out.println("Diagnosis Added in Management Tab");
    }

    @Test(dependsOnMethods = "addDiagnosis", priority = 7)
    public void addProcedure() {

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[6]/div[2]/div[1]/div/div/div[2]/span"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div/input"))).sendKeys("881");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div[2]/div/div[2]/div[2]/span/span[3]"))).click();
            System.out.println("Procedure selected from suggestions.");
        } catch (TimeoutException e) {
            System.out.println("No suggestion found. Clicking 'Add New' ...");

            try {
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div[2]/div/div[2]/div[1]/span/span[2]"))).click();
                System.out.println("'Add New' option clicked.");
            } catch (TimeoutException ex) {
                System.out.println("No suggestion or Add New found.");
            }
        }

        WebElement startdate = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"datepicker\"]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" +
                dobFormatted + "', true);", startdate);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"subProcedures\"]")))
                .sendKeys("Automated SubFilter");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"proc_notes\"]")))
                .sendKeys("Automated Notes");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[6]/div[2]/div"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[8]/button"))).click();

        System.out.println("Procedure Added in Management Tab");
    }

    @Test(dependsOnMethods = "addProcedure", priority = 8)
    public void addFilter() throws InterruptedException {

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[8]/div[2]/div[1]/div/div/div[2]/span"))).click();

        String filterText = "Automated Filter";

        WebElement filterInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/input")
        ));

        filterInput.clear();
        filterInput.sendKeys(filterText);

        Thread.sleep(500);

        List<WebElement> suggestions = driver.findElements(By.xpath(
                "//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[2]/div/span/span[2]"
        ));

        boolean filterFound = false;

        for (WebElement s : suggestions) {
            if (s.getText().equalsIgnoreCase(filterText)) {
                s.click();
                filterFound = true;
                System.out.println("Filter selected from suggestions.");
                break;
            }
        }

        if (!filterFound) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/span/span")
                )).click();
                System.out.println("'Add New' option clicked for Filter.");
            } catch (TimeoutException ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        WebElement startdate = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/input")));

        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" +
                dobFormatted + "', true);", startdate);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div/button"))).click();

        System.out.println("AdHOC Filter Added in Management Tab");
    }
}
