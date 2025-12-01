package Staging;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.function.Function;

public class BaseClass {

    public static WebDriver driver;
    public static WebDriverWait wait;
    public static String P_FirstName;
    public static String P_LastName;
    public static String P_DOB;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    @BeforeSuite(alwaysRun = true)
    public void setUp() {

    	ChromeOptions options = new ChromeOptions();

    	options.addArguments("--no-sandbox");
    	options.addArguments("--disable-dev-shm-usage");
    	options.addArguments("--disable-gpu");
    	options.addArguments("--window-size=1920,1080");
    	options.addArguments("--remote-allow-origins=*");
    	options.addArguments("--disable-search-engine-choice-screen");

    	// ⭐ THE FIX ⭐
    	options.addArguments("--disable-features=OptimizationGuideModelDownloading");


        driver = WebDriverSetup.getDriver(options);

        // explicit wait used across tests
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);

        // small global implicit wait to help findBy calls (kept small)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        System.out.println("===== DRIVER STARTED =====");
    }

    @AfterSuite
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
    }


    // -------------------------------------
    //     GLOBAL MODAL HANDLER
    // -------------------------------------
    public static void waitForModalToClose() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".modal-background")));
        } catch (Exception ignored) {}
    }

    // -------------------------------------
    //     GLOBAL SAFE CLICK
    // -------------------------------------
    public static WebElement safeClick(By locator) {

        waitForModalToClose();  // ensure modal is gone

        // wait until clickable then attempt click with fallbacks
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

        try {
            element.click();
            return element;
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            // try to re-find and click
        } catch (Exception e) {
            // fallback to JS click if other exceptions occur
        }

        // retry sequence: re-locate, scroll into view, try click, finally JS click
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            try {
                el.click();
                return el;
            } catch (Exception ignored) {}
            // final fallback
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception ignored) {}
		return element;
    }

    // SAFE SENDKEYS (used rarely)
    public static void safeSendKeys(By locator, String text) {
        waitForModalToClose();
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.clear();
        element.sendKeys(text);
    }

    // small helper: wait until element not stale and present
    public static WebElement waitForPresence(final By locator, Duration timeout) {
        WebDriverWait localWait = new WebDriverWait(driver, timeout);
        return localWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForPresence(final By locator) {
        return waitForPresence(locator, DEFAULT_TIMEOUT);
    }
    
    public WebElement scrollIntoView(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        return element;
    }

//    public static String dataSource;  // Global access
//    @BeforeClass
//    @Parameters("dataSource")
//    public void setup(String ds) {
//        dataSource = ds;         // <-- Uses value from testng.xml
//        System.out.println("Data source selected: " + dataSource);
//    }
}
