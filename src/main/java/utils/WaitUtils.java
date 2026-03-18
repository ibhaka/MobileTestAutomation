package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class providing explicit wait methods.
 * All interactions should go through this class to avoid flakiness.
 */
public class WaitUtils {

    private static AndroidDriver getDriver() {
        return DriverManager.getDriver();
    }

    private static WebDriverWait getWait() {
        int timeout = Integer.parseInt(
                DriverManager.getProps().getProperty("explicit.wait", "20"));
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
    }

    private static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
    }

    public static WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static List<WebElement> waitForVisibilityOfAll(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean isElementVisible(By locator) {
        try {
            return getWait(5).until(
                    ExpectedConditions.visibilityOfElementLocated(locator)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementVisible(By locator, int timeoutSeconds) {
        try {
            return getWait(timeoutSeconds).until(
                    ExpectedConditions.visibilityOfElementLocated(locator)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static void click(By locator) {
        waitForClickable(locator).click();
    }

    public static void sendKeys(By locator, String text) {
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    public static String getText(By locator) {
        return waitForVisibility(locator).getText();
    }
}
