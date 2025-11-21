import org.openqa.selenium.chrome.ChromeOptions;

@BeforeSuite
public void startDriver() {

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");

    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    driver.manage().window().maximize();
    System.out.println("===== DRIVER STARTED (BeforeSuite) =====");
}
