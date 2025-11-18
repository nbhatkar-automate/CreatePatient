package Staging;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class StableUtils {

    public static void clickWithRetries(WebDriver driver, WebDriverWait wait, By locator) {
        int attempts = 0;
        while (attempts < 4) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollTo(driver, el);
                try {
                    el.click();
                    return;
                } catch (Exception clickEx) {
                    clickElementViaJS(driver, el);
                    return;
                }
            } catch (Exception e) {
                attempts++;
                sleep(300);
            }
        }
        WebElement el = driver.findElement(locator);
        clickElementViaJS(driver, el);
    }

    public static void sendKeysWithRetries(WebDriver driver, WebDriverWait wait, By locator, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                scrollTo(driver, el);
                el.clear();
                el.sendKeys(text);
                return;
            } catch (Exception e) {
                attempts++;
                sleep(200);
            }
        }
        WebElement el = driver.findElement(locator);
        scrollTo(driver, el);
        el.clear();
        el.sendKeys(text);
    }

    public static void setFlatpickrDate(WebDriver driver, WebDriverWait wait, By locator, String date) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollTo(driver, el);
        try {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0]._flatpickr.setDate(arguments[1], true);", el, date);
            sleep(300);
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                .executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
                    el, date
                );
            sleep(300);
        }
    }

    public static void scrollToElement(WebDriver driver, WebDriverWait wait, By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollTo(driver, el);
    }

    public static void scrollTo(WebDriver driver, WebElement el) {
        try {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
            sleep(150);
        } catch (Exception ignored) {}
    }

    public static void clickElementViaJS(WebDriver driver, WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception e) {
            try {
                new Actions(driver).moveToElement(el).click().perform();
            } catch (Exception ex) {
                throw new RuntimeException("Unable to click element", ex);
            }
        }
    }

    public static void waitForInvisibility(WebDriver driver, WebDriverWait wait, By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForVisibility(WebDriver driver, WebDriverWait wait, By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForNoFlatpickr(WebDriver driver, WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".flatpickr-calendar")));
        } catch (Exception ignored) {}
    }

    public static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
