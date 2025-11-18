package Staging;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PatientCreate {

    static WebDriver driver;
    static WebDriverWait wait;

    String firstName, lastName, email, phone, dobFormatted;

    @BeforeClass
    public void setup() {
        driver = LoginPage.driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        generatePatientData();
        openAddPatientForm();
    }

    /** Generate fake patient data */
    public void generatePatientData() {
        Faker faker = new Faker(new Locale("en-US"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = String.format(
                "%s.%s%d@%s",
                firstName.toLowerCase(),
                lastName.toLowerCase(),
                faker.number().numberBetween(100, 999),
                "testdomain.io"
        );
        phone = faker.phoneNumber().subscriberNumber(10);

        int year = 1980 + new java.util.Random().nextInt(25);
        int month = 1 + new java.util.Random().nextInt(12);
        int day = 1 + new java.util.Random().nextInt(28);

        LocalDate dob = LocalDate.of(year, month, day);
        dobFormatted = dob.format(formatter);
    }

    /** Open Quick Add Patient form + fill basic fields */
    public void openAddPatientForm() {

        // Search box
        WebElement addPatientInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Search or Add Patient (name, cell, or email)']")));
        addPatientInput.click();

        // Click quick add
        WebElement addNew = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(), 'Quick Add New Patient')]")));
        addNew.click();

        // Enter phone
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[1]/div/div[1]/input")))
                .sendKeys(phone);

        // Click "Fill Out Full Info"
        WebElement fillFullForm = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#app-component > div.modal > div.modal-box-container > div > div.patient-add-form.mobile > div.patient-add-short > div:nth-child(2) > div.fill-out-text > div")));
        scrollIntoView(fillFullForm);
        fillFullForm.click();

        // Enter first name
        driver.findElement(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[1]/input"))
                .sendKeys(firstName);

        // Enter last name
        driver.findElement(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[2]/input"))
                .sendKeys(lastName);

        // Enter email
        driver.findElement(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[3]/input"))
                .sendKeys(email);

        // DOB (flatpickr)
        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Date of birth']")));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0]._flatpickr.setDate('" + dobFormatted + "', true);", dateInput);

        sleep(800);

        // Scroll modal into view
        WebElement modalContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#app-component > div.modal > div.modal-box-container")));
        scrollIntoView(modalContainer);
    }

    /** Select Sex */
   @Test
public void selectSex() {

    // Open dropdown
    By dropdownXpath = By.xpath("//label[contains(text(),'Sex')]/following::div[contains(@class,'custom-select')][1]");
    WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownXpath));

    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);

    // Select option by visible text (NO index)
    By optionXpath = By.xpath("//div[@class='option' and text()='Male']");
    WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionXpath));

    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

    System.out.println("Sex selected: Male");
}

    /** Select Gender */
    @Test(dependsOnMethods = "selectSex")
    public void selectGender() {

        WebElement genderBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/span/button")));

        scrollIntoView(genderBtn);
        genderBtn.click();

        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/div/div[1]/div[4]/span/input");
        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/div/div[1]/div[7]/span/input");

        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/div/div[2]/button");
    }

    /** Select Race */
    @Test(dependsOnMethods = "selectGender")
    public void selectRace() {

        WebElement raceBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/span/button")));

        scrollIntoView(raceBtn);
        raceBtn.click();

        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[1]/div[2]/span/input");
        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[1]/div[4]/span/input");
        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[1]/div[5]/span/input");

        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[2]/button");
    }

    /** Select Ethnicity */
    @Test(dependsOnMethods = "selectRace")
    public void selectEthnicity() {

        WebElement ethBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/span/button")));

        scrollIntoView(ethBtn);
        ethBtn.click();

        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/div/div[1]/div[6]/span/input");
        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/div/div[1]/div[9]/span/input");

        clickWithScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/div/div[2]/button");
    }

    /** Final Add Button */
    @Test(dependsOnMethods = "selectEthnicity")
    public void clickAddButton() {

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[2]/button")));

        scrollIntoView(addBtn);
        addBtn.click();

        // Handle modal if appears
        try {
            WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Yes Proceed')]")));
            yesButton.click();
            System.out.println("Invalid number modal appeared. Clicked 'Yes Proceed'.");
        } catch (Exception e) {
            System.out.println("No invalid number modal appeared.");
        }

        // Wait for loader to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[@id='main-contents']/div/div/div/div[4]/div/div[1]/div/div/div/div/img")));

        System.out.println("Patient Added: " + firstName + " " + lastName + " | " + phone + " | " + email);
    }

    // Utility scroll
    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        sleep(400);
    }

    private void clickWithScroll(String xpath) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        scrollIntoView(el);
        el.click();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }
}
