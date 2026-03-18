package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * City search overlay page.
 * Rows show: "Istanbul Europe  Istanbul, Türkiye"
 */
public class CitySearchPage extends BasePage {

    private final By searchInput = By.xpath("//android.widget.EditText");

    public void typeCity(String city) {
        sendKeys(searchInput, city);
    }

    /**
     * Selects the city whose text STARTS WITH the exact city name.
     * e.g. cityName="Istanbul Europe" matches "Istanbul Europe Istanbul, Türkiye"
     */
    public void selectCity(String cityName) {
        // Wait a moment for results to load
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        // Exact text match first
        By exactLocator = By.xpath(
                "//android.widget.TextView[@text='" + cityName + "']");
        if (isVisible(exactLocator, 3)) {
            click(exactLocator);
            return;
        }

        // Starts-with match (handles "Istanbul Europe Istanbul, Türkiye" format)
        By startsWithLocator = By.xpath(
                "//android.widget.TextView[starts-with(@text, '" + cityName + "')]");
        if (isVisible(startsWithLocator, 3)) {
            List<WebElement> matches = findElements(startsWithLocator);
            if (!matches.isEmpty()) {
                matches.get(0).click();
                return;
            }
        }

        // Contains match fallback
        By containsLocator = By.xpath(
                "//android.widget.TextView[contains(@text, '" + cityName + "')]");
        if (isVisible(containsLocator, 3)) {
            List<WebElement> matches = findElements(containsLocator);
            if (!matches.isEmpty()) {
                matches.get(0).click();
                return;
            }
        }

        throw new RuntimeException("City not found in search results: " + cityName);
    }

    public void selectFirstResult() {
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        By firstResult = By.xpath(
                "(//android.widget.TextView[string-length(@text) > 3 "
              + "and not(@text='Clean') and not(@text='FROM') and not(@text='TO') "
              + "and not(@text='Other Stations')])[1]");
        click(firstResult);
    }
}
