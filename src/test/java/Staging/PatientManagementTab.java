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

public class PatientManagementTab {

    static WebDriver driver;
    static WebDriverWait wait;
    String dobFormatted;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    // sharing for next class
    public static WebDriver sharedDriver;
    public static WebDriverWait sharedWait;
    
    @BeforeClass
    public void setup() {
        driver = PatientCreate.driver;
        wait = PatientCreate.wait;

        // Generate random DOB
        int year = 2000 + new java.util.Random().nextInt(25);
        int month = 1 + new java.util.Random().nextInt(12);
        int day = 1 + new java.util.Random().nextInt(28);
        LocalDate dob = LocalDate.of(year, month, day);
        dobFormatted = dob.format(formatter);
    }

    @Test(dependsOnMethods = "Staging.PatientCreate.clickAddButton", priority = 1)
    public void openPatientAndNavigateToManagementTab() {
        // Open patient profile
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"main-contents\"]/div/div/div/div[2]/div/div/div[1]/span[2]/span[2]"))).click();

        // Navigate to Management Tab
        WebElement ManagementTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[1]/span[3]")));
        ManagementTab.click();
        System.out.println("Navigated to Management Tab");
    }
    
    @Test(dependsOnMethods = "openPatientAndNavigateToManagementTab", priority = 2)
    public void addTask() {
        // Click add task
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[1]/div[2]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/input"))).sendKeys("Filled Via Automation");
        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[6]/div[2]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", dateInput);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[7]/div[2]/div/div[2]/div[1]"))).sendKeys("Automated Comment");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[8]/button"))).click();
        System.out.println("Task Added in Management Tab");
    }

    @Test(dependsOnMethods = "addTask", priority = 3)
    public void addPatientNote() {
    // Step 1: Send ESCAPE to close floating datepicker
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
 
   // Step 2: Wait until datepicker input is no longer visible
        WebElement floatingInput = driver.findElement(By.xpath("//input[contains(@class, 'flatpickr-input')]"));
        wait.until(ExpectedConditions.invisibilityOf(floatingInput));

  // Step 3: Now continue to click "Add Note"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[2]/div[2]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div[2]/div/div[2]/div[1]"))).sendKeys("Automated Note");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[4]/button"))).click();
         System.out.println("Note Added in Management Tab");
        }


    @Test(dependsOnMethods = "addPatientNote", priority = 4)
    public void addCallLogEntry() throws InterruptedException {
        Actions actions = new Actions(driver);

        // Step 1: Press ESC to close overlay
        actions.sendKeys(Keys.ESCAPE).perform();
        Thread.sleep(2000);  // Let the UI respond

     // Scroll down slightly (100 pixels) to reveal the call log checkbox
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");
        
     // Wait until modal background disappears (invisible)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-background")));

        // Step 4: Open Call Log modal
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
            "//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[3]/div[1]/input"
        ))).click();

        // Step 5: Wait for modal to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
            "//*[@id=\"app-component\"]/div[1]/div[2]/div"
        )));

        // Step 6: Enter Text
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[4]/div/div[2]/div[1]"))).sendKeys("Automated Entry");

        // Step 7: Click submit
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
            "//*[@id=\"app-component\"]/div[1]/div[2]/div/div[5]/button"
        ))).click();
        

        // Step 9: Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
            "//*[@id=\"app-component\"]/div[1]/div[2]/div"
        )));

        System.out.println("Call Log Entry Added in Management Tab");
    }

    @Test(dependsOnMethods = "addCallLogEntry", priority = 5)
    public void addMedications() throws InterruptedException {

        // Scroll slightly
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");

        // Click "Add Medications"
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[4]/div[2]/div[1]/div/div/div[2]/img"))
        ).click();

        // Type "Syrup" into the search box
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/input")));
        searchBox.sendKeys("Syrup");  // replaced JS with standard Selenium typing
        searchBox.click();

        // Wait 2-3 seconds for suggestions to appear (adjust if needed)
        Thread.sleep(3000);

        // Now get suggestions safely
        List<WebElement> suggestions = driver.findElements(By.cssSelector(".selection-list .selection-item"));

        if (suggestions.isEmpty()) {
            throw new RuntimeException("No suggestions found.");
        }

        boolean found = false;

        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().toLowerCase().trim();
            System.out.println("SUGGESTION: " + text);
            if (text.contains("panatuss")) {
                suggestion.click();  // replaced JS click with standard Selenium click
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("'Syrup' not found in suggestions.");
        }

        // Set Start Date
        WebElement startdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[4]/div[2]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", startdate);

        // Set Stop Date
        WebElement stopdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[5]/div[2]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", stopdate);

        // Click Save
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div[2]/button"))).click();

        System.out.println("Medication Added in Management Tab");
    }


    @Test(dependsOnMethods = "addMedications", priority = 6)
    public void addDiagnosis() {
    // Step 1: Send ESCAPE to close floating datepicker
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
 
   // Step 2: Wait until datepicker input is no longer visible
        WebElement floatingInput = driver.findElement(By.xpath("//input[contains(@class, 'flatpickr-input')]"));
        wait.until(ExpectedConditions.invisibilityOf(floatingInput));
        
  // Step 3: Now continue to click "Add Diagnosis"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[5]/div[2]/div[1]/div/div/div[2]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/span[2]/input"))).sendKeys("test");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/button"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]")));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/ul/li[4]"))).click();
            System.out.println("Diagnosis selected from suggestions.");
        } catch (TimeoutException e) {
            System.out.println("No suggestion found. Attempting to click 'Add New'...");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/ul/li[1]"))).click();
                System.out.println("'Add New' option clicked for diagnosis.");
            } catch (TimeoutException ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        System.out.println("Diagnosis Added in Management Tab");
    }

    @Test(dependsOnMethods = "addDiagnosis", priority = 7)
    public void addProcedure() {
    // Step 1: Send ESCAPE to close floating datepicker
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
        
     // Scroll down slightly (300 pixels) to reveal the call log button
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
        
   // Step 3: Now continue to click "Add Procedure"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[6]/div[2]/div[1]/div/div/div[2]/span"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div/input"))).sendKeys("881");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div[2]/div/div[2]/div[2]/span/span[3]"))).click();
            System.out.println("Procedure selected from suggestions.");
        } catch (TimeoutException e) {
            System.out.println("No suggestion found. Attempting to click 'Add New'...");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[2]/div[2]/div/div[2]/div[1]/span/span[2]"))).click();
                System.out.println("'Add New' option clicked for Procedure.");
            } catch (TimeoutException ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        WebElement startdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"datepicker\"]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", startdate);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"subProcedures\"]"))).sendKeys("Automated SubFilter");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"proc_notes\"]"))).sendKeys("Automated Notes");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[6]/div[2]/div"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/form/div[8]/button"))).click();
        System.out.println("Procedure Added in Management Tab");
    }

    @Test(dependsOnMethods = "addProcedure", priority = 8)
    public void addFilter() throws InterruptedException {
   // Step 1: Send ESCAPE to close floating datepicker
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
        
     // Scroll down slightly (300 pixels) to reveal the call log button
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
        
     // Step 3: Now continue to click "Add AdHoc Filter"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-contents\"]/div/div/div/div/div[5]/div[2]/div/div/div/div[8]/div[2]/div[1]/div/div/div[2]/span"))).click();

        // Type the filter text
        String filterText = "Automated Filter";
        WebElement filterInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/input")
        ));
        filterInput.clear();
        filterInput.sendKeys(filterText);

        // Wait a short moment for suggestions to load
        Thread.sleep(500); 

        // Get all suggestion elements
        List<WebElement> suggestions = driver.findElements(By.xpath(
            "//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[2]/div/span/span[2]"
        ));

        // Flag to check if filter already exists
        boolean filterFound = false;

        // Loop through suggestions to see if the filter already exists
        for(WebElement s : suggestions) {
            if(s.getText().equalsIgnoreCase(filterText)) {
                s.click();
                filterFound = true;
                System.out.println("Filter selected from suggestions.");
                break;
            }
        }

        // If filter not found, click "Add New"
        if(!filterFound) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/span/span")
                )).click();
                System.out.println("'Add New' option clicked for Filter.");
            } catch (TimeoutException ex) {
                System.out.println("Neither suggestion nor 'Add New' option found.");
            }
        }

        WebElement startdate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/input")));
        ((JavascriptExecutor) driver).executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", startdate);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[3]/div/button"))).click();
        System.out.println("AdHOC Filter Added in Management Tab");

        // sharing for next class
        sharedDriver = driver;
        sharedWait = wait;
        }
}
