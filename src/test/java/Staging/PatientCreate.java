package Staging;

import com.github.javafaker.Faker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientCreate extends BaseClass {

    String firstName, lastName, email, phone, dob;

    // Excel file path
    private static final String EXCEL_PATH = System.getProperty("user.dir")
            + "/src/test/resources/testdata/PatientData.xlsx";

    // -----------------------------
    // BEFORE CLASS → DECIDE DATA SOURCE
    // -----------------------------
    
    public static String dataSource; // Global access

    @BeforeClass
    @Parameters("dataSource")
    public void setup(String ds) {
        // Initialize driver and wait from BaseClass
        driver = BaseClass.driver;
        wait = BaseClass.wait;

        // Set data source from TestNG XML parameter
        dataSource = ds;
        System.out.println("Data source selected: " + dataSource);

        // Load patient data based on the selected data source
        if ("faker".equalsIgnoreCase(dataSource)) {
            System.out.println("dataSource=faker → Using Faker ONLY");
            setFakerData();
        } else if ("excel".equalsIgnoreCase(dataSource)) {
            System.out.println("dataSource=excel → Trying Excel");
            loadPatientDataExcelOnly(); // throw error if Excel missing
        } else {
            throw new RuntimeException("Unknown dataSource: " + dataSource);
        }

        // Set DOB globally
        BaseClass.P_DOB = dob;

        // Open Add Patient form & fill first screen
        openAddPatientForm();
    }

    // -----------------------------
    // LOAD DATA STRICTLY FROM EXCEL
    // -----------------------------
    private void loadPatientDataExcelOnly() {
        try {
            File f = new File(EXCEL_PATH);

            if (!f.exists()) {
                throw new RuntimeException("Excel file not found at: " + EXCEL_PATH);
            }

            FileInputStream fis = new FileInputStream(f);
            XSSFWorkbook wb = new XSSFWorkbook(fis);

            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(1); // Row 2 (index 1)

            if (row != null) {
                String excelFN  = getStringCell(row, 0);
                String excelLN  = getStringCell(row, 1);
                String excelEM  = getStringCell(row, 2);
                String excelPH  = getStringCell(row, 3);
                String excelDOB = getStringCell(row, 4);

                if (!excelFN.isEmpty()) firstName = excelFN;
                if (!excelLN.isEmpty()) lastName  = excelLN;
                if (!excelEM.isEmpty()) email     = excelEM;
                if (!excelPH.isEmpty()) phone     = excelPH;
                if (!excelDOB.isEmpty()) dob      = excelDOB;
            }

            wb.close();
            fis.close();

        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel file → " + e.getMessage(), e);
        }
    }


    // -----------------------------
    // BEGINNER-FRIENDLY EXCEL READER
    // Fixes phone like 3.122E9 → 31225558479
    // -----------------------------
    private String getStringCell(Row row, int col) {
        try {
            Cell cell = row.getCell(col);

            if (cell == null) return "";

            if (cell.getCellType() == CellType.STRING)
                return cell.getStringCellValue().trim();

            if (cell.getCellType() == CellType.NUMERIC) {
                long longVal = (long) cell.getNumericCellValue();
                return String.valueOf(longVal);
            }

            return cell.toString().trim();

        } catch (Exception e) {
            return "";
        }
    }

    // -----------------------------
    // GENERATE FAKER DATA (DEFAULT)
    // -----------------------------
    private void setFakerData() {

        Faker faker = new Faker(new Locale("en-US"));

        firstName = faker.name().firstName();
        lastName  = faker.name().lastName();

        email = firstName.toLowerCase() + "." + lastName.toLowerCase()
                + faker.number().numberBetween(100, 999)
                + "@testmail.com";

        phone = faker.number().digits(10);

        Date fakerDob = faker.date().birthday(25, 65);
        dob = new SimpleDateFormat("MM/dd/yyyy").format(fakerDob);
    }

    // -----------------------------
    // OPEN FORM & FILL BASIC DETAILS
    // -----------------------------
    private void openAddPatientForm() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Search or Add Patient (name, cell, or email)']")))
                .click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(), 'Quick Add New Patient')]")))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[1]/div[1]/div/div[1]/input")))
                .sendKeys(phone);

        WebElement fullFormBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#app-component > div.modal > div.modal-box-container > div > div.patient-add-form.mobile > div.patient-add-short > div:nth-child(2) > div.fill-out-text > div")));

        fullFormBtn.click();

        driver.findElement(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[1]/input"))
                .sendKeys(firstName);

        driver.findElement(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[2]/input"))
                .sendKeys(lastName);

        driver.findElement(By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[3]/input"))
                .sendKeys(email);

        WebElement dobInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Date of birth']")));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0]._flatpickr.setDate('" + dob + "', true);", dobInput);

        sleep(600);
    }

    // -----------------------------
    // SEX
    // -----------------------------
    @Test
    public void selectSex() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#app-component > div.modal > div.modal-box-container > div > div.patient-add-form.mobile > div.patient-add-long > div.patient-long-form.patient-add-long > div > div:nth-child(6) > div.custom-select-container > div.custom-select")));

        scroll(dropdown);
        dropdown.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[6]/div[2]/div[2]/div[4]")));

        scroll(option);
        option.click();
    }

    // -----------------------------
    // GENDER
    // -----------------------------
    @Test(dependsOnMethods = "selectSex")
    public void selectGender() {

        WebElement genderBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/span/button")));

        scroll(genderBtn);
        genderBtn.click();

        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/div/div[1]/div[4]/span/input");
        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/div/div[1]/div[7]/span/input");

        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[7]/span/div/div[2]/button");
    }

    // -----------------------------
    // RACE
    // -----------------------------
    @Test(dependsOnMethods = "selectGender")
    public void selectRace() {

        WebElement raceBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/span/button")));

        scroll(raceBtn);
        raceBtn.click();

        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[1]/div[2]/span/input");
        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[1]/div[4]/span/input");
        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[1]/div[5]/span/input");

        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[8]/span/div/div[2]/button");
    }

    // -----------------------------
    // ETHNICITY
    // -----------------------------
    @Test(dependsOnMethods = "selectRace")
    public void selectEthnicity() {

        WebElement ethBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/span/button")));

        scroll(ethBtn);
        ethBtn.click();

        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/div/div[1]/div[6]/span/input");
        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/div/div[1]/div[9]/span/input");

        clickScroll("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[1]/div/div[9]/span/div/div[2]/button");
    }

    // -----------------------------
    // FINAL ADD BUTTON
    // -----------------------------
    @Test(dependsOnMethods = "selectEthnicity")
    public void clickAddButton() {

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"app-component\"]/div[1]/div[2]/div/div[2]/div[2]/div[2]/button")));

        scroll(addBtn);
        
        addBtn.click();

        try {
            WebElement yesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Yes Proceed')]")));
            yesButton.click();
        } catch (Exception ignore) {}

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//*[@id='main-contents']/div/div/div/div[4]/div/div[1]/div/div/div/div/img")));

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='main-contents']/div/div/div/div[2]/div/div/div[1]/span[2]/span[2]")))
                .click();

        System.out.println("Patient Added → " + firstName + " " + lastName +
                " | " + phone + " | " + email);
    }

    // -----------------------------
    // Utilities
    // -----------------------------
    private void scroll(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        sleep(300);
    }

    private void clickScroll(String xpath) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        scroll(el);
        el.click();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }
}
