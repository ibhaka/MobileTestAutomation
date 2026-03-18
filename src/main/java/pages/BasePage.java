package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.DriverManager;
import utils.WaitUtils;

import java.util.List;

/**
 * Base class for all Page Objects.
 * Provides common interaction methods using WaitUtils.
 */
public class BasePage {

    protected AndroidDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected void click(By locator) {
        WaitUtils.click(locator);
    }

    protected void sendKeys(By locator, String text) {
        WaitUtils.sendKeys(locator, text);
    }

    protected String getText(By locator) {
        return WaitUtils.getText(locator);
    }

    protected WebElement findElement(By locator) {
        return WaitUtils.waitForVisibility(locator);
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    protected boolean isVisible(By locator) {
        return WaitUtils.isElementVisible(locator);
    }

    protected boolean isVisible(By locator, int timeoutSeconds) {
        return WaitUtils.isElementVisible(locator, timeoutSeconds);
    }
}
