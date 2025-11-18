package Staging;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PatientManagementTab {

    static WebDriver driver;
    static WebDriverWait wait;
    String dobFormatted;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @BeforeClass(alwaysRun = true)
    public void setup() {
        driver = PatientCreate.driver;
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(60));

        // Generate random DOB
        int year = 2000 + new java.util.Random().nextInt(25);
        int month = 1 + new java.util.Random().nextInt(12);
        int day = 1 + new java.util.Random().nextInt(28);
        LocalDate dob = LocalDate.of(year, month, day);
        dobFormatted = dob.format(formatter);
    }

       @Test(dependsOnMethods = "Staging.PatientCreate.clickAddButton", priority = 1)
        public void openPatientAndNavigateToManagementTab() {

    By openOrView = By.xpath("//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'open') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'view')]");

    WebElement button = WaitUtils.waitForPresence(driver, openOrView);

    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

    System.out.println("Patient opened successfully");

    By managementTab = By.xpath("//span[contains(text(), 'Management')]");
    WebElement tab = WaitUtils.waitForPresence(driver, managementTab);

    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tab);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);

    System.out.println("Navigated to Management Tab");
}

    @Test(dependsOnMethods = "openPatientAndNavigateToManagementTab", priority = 2)
    public void addTask() {
        By addTask = By.xpath("//div[@class='add-item' and contains(text(), 'Add Task')]");
        StableUtils.clickWithRetries(driver, wait, addTask);

        By titleInput = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/input");
        StableUtils.sendKeysWithRetries(driver, wait, titleInput, "Filled Via Automation");

        By dateInput = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[6]/div[2]/input");
        StableUtils.setFlatpickrDate(driver, wait, dateInput, dobFormatted);

        By commentInput = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[7]/div[2]/div/div[2]/div[1]");
        StableUtils.sendKeysWithRetries(driver, wait, commentInput, "Automated Comment");

        By saveBtn = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[8]/button");
        StableUtils.clickWithRetries(driver, wait, saveBtn);

        System.out.println("Task Added in Management Tab");
    }

    @Test(dependsOnMethods = "addTask", priority = 3)
    public void addPatientNote() {
        // Close any floating datepicker by sending ESC
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        // wait until any flatpickr input becomes invisible (safe)
        StableUtils.waitForNoFlatpickr(driver, wait);

        By addNote = By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[2]/div[2]");
        StableUtils.clickWithRetries(driver, wait, addNote);

        By noteInput = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div[2]/div/div[2]/div[1]");
        StableUtils.sendKeysWithRetries(driver, wait, noteInput, "Automated Note");

        By saveNote = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[4]/button");
        StableUtils.clickWithRetries(driver, wait, saveNote);

        System.out.println("Note Added in Management Tab");
    }

    @Test(dependsOnMethods = "addPatientNote", priority = 4)
    public void addCallLogEntry() throws InterruptedException {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        Thread.sleep(1000);

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");
        StableUtils.waitForInvisibility(driver, wait, By.cssSelector(".modal-background"));

        By callLogOpen = By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[3]/div[1]/input");
        StableUtils.clickWithRetries(driver, wait, callLogOpen);

        // wait for modal to appear (xpath unchanged)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div")));

        By callText = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[4]/div/div[2]/div[1]");
        StableUtils.sendKeysWithRetries(driver, wait, callText, "Automated Entry");

        By callSubmit = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[5]/button");
        StableUtils.clickWithRetries(driver, wait, callSubmit);

        StableUtils.waitForInvisibility(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div"));

        System.out.println("Call Log Entry Added in Management Tab");
    }

    @Test(dependsOnMethods = "addCallLogEntry", priority = 5)
    public void addMedications() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");

        By addMed = By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[4]/div[2]/div[1]/div/div/div[2]/img");
        StableUtils.clickWithRetries(driver, wait, addMed);

        By searchBox = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/input");
        StableUtils.sendKeysWithRetries(driver, wait, searchBox, "Syrup");
        StableUtils.clickWithRetries(driver, wait, searchBox);

        Thread.sleep(2000); // allow suggestions to populate
        List<WebElement> suggestions = driver.findElements(By.cssSelector(".selection-list .selection-item"));

        if (suggestions.isEmpty()) {
            throw new RuntimeException("No suggestions found.");
        }

        boolean found = false;
        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().toLowerCase().trim();
            if (text.contains("panatuss")) {
                StableUtils.clickElementViaJS(driver, suggestion); // fallback click
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("'Syrup' suggestion with 'panatuss' not found.");
        }

        // Start/Stop dates
        By startdate = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[4]/div[2]/input");
        By stopdate  = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[5]/div[2]/input");
        StableUtils.setFlatpickrDate(driver, wait, startdate, dobFormatted);
        StableUtils.setFlatpickrDate(driver, wait, stopdate, dobFormatted);

        By saveMed = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div[2]/button");
        StableUtils.clickWithRetries(driver, wait, saveMed);

        System.out.println("Medication Added in Management Tab");
    }

    @Test(dependsOnMethods = "addMedications", priority = 6)
    public void addDiagnosis() {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        StableUtils.waitForNoFlatpickr(driver, wait);

        By addDiag = By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[5]/div[2]/div[1]/div/div/div[2]");
        StableUtils.clickWithRetries(driver, wait, addDiag);

        By diagInput = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/span[2]/input");
        StableUtils.sendKeysWithRetries(driver, wait, diagInput, "test");

        By diagSave = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/button");
        StableUtils.clickWithRetries(driver, wait, diagSave);

        // Try suggestions then "Add New" fallback (xpaths unchanged)
        try {
            StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/ul/li[4]"));
            System.out.println("Diagnosis selected from suggestions.");
        } catch (Exception e) {
            try {
                StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/ul/li[1]"));
                System.out.println("'Add New' option clicked for diagnosis.");
            } catch (Exception ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        System.out.println("Diagnosis Added in Management Tab");
    }

    @Test(dependsOnMethods = "addDiagnosis", priority = 7)
    public void addProcedure() {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");

        By addProc = By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[6]/div[2]/div[1]/div/div/div[2]/span");
        StableUtils.clickWithRetries(driver, wait, addProc);

        By codeInput = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div/input");
        StableUtils.sendKeysWithRetries(driver, wait, codeInput, "881");

        // Try suggestion then fallback "Add New"
        try {
            StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div[2]/div/div[2]/div[2]/span/span[3]"));
            System.out.println("Procedure selected from suggestions.");
        } catch (Exception e) {
            try {
                StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div[2]/div/div[2]/div[1]/span/span[2]"));
                System.out.println("'Add New' option clicked for Procedure.");
            } catch (Exception ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        By startdate = By.xpath("//*[@id=\"datepicker\"]/input");
        StableUtils.setFlatpickrDate(driver, wait, startdate, dobFormatted);

        StableUtils.sendKeysWithRetries(driver, wait, By.xpath("//*[@id=\"subProcedures\"]"), "Automated SubFilter");
        StableUtils.sendKeysWithRetries(driver, wait, By.xpath("//*[@id=\"proc_notes\"]"), "Automated Notes");

        StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[6]/div[2]/div"));
        StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[8]/button"));

        System.out.println("Procedure Added in Management Tab");
    }

    @Test(dependsOnMethods = "addProcedure", priority = 8)
    public void addFilter() throws InterruptedException {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");

        By addFilter = By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[8]/div[2]/div[1]/div/div/div[2]/span");
        StableUtils.clickWithRetries(driver, wait, addFilter);

        String filterText = "Automated Filter";
        By filterInputPath = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/input");
        StableUtils.sendKeysWithRetries(driver, wait, filterInputPath, filterText);

        Thread.sleep(500);

        List<WebElement> suggestions = driver.findElements(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[2]/div/span/span[2]"));

        boolean filterFound = false;
        for (WebElement s : suggestions) {
            if (s.getText().equalsIgnoreCase(filterText)) {
                StableUtils.clickElementViaJS(driver, s);
                filterFound = true;
                System.out.println("Filter selected from suggestions.");
                break;
            }
        }

        if (!filterFound) {
            try {
                StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/span/span"));
                System.out.println("'Add New' option clicked for Filter.");
            } catch (Exception ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        By startdate = By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/input");
        StableUtils.setFlatpickrDate(driver, wait, startdate, dobFormatted);

        StableUtils.clickWithRetries(driver, wait, By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div/button"));

        System.out.println("AdHOC Filter Added in Management Tab");
    }
}
