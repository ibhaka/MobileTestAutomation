package pages;

import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // Tab Bar
    private final By busTab =
            By.xpath("//android.widget.TextView[@text='Bus']");
    private final By hotelTab =
            By.xpath("//android.widget.TextView[@text='Hotel']");

    // Bus search fields
    private final By fromField =
            By.xpath("//android.widget.TextView[@text='FROM']/following-sibling::android.widget.TextView[1]");
    private final By toField =
            By.xpath("//android.widget.TextView[@text='TO']/following-sibling::android.widget.TextView[1]");
    private final By searchButton =
            By.xpath("//android.widget.Button[@text='SEARCH']");

    // ─────────────────────────────────────────────────────────────────────────

    public void selectBusTab() {
        // Wait for tab to be visible then click
        findElement(busTab);
        click(busTab);
        // Verify Bus search panel appeared (FROM field visible)
        findElement(fromField);
    }

    public void selectHotelTab() {
        click(hotelTab);
    }

    public void waitForHomeScreen() {
        findElement(searchButton);
    }

    public void tapFromField() {
        click(fromField);
    }

    public void tapToField() {
        click(toField);
    }

    public void tapBusSearchButton() {
        click(searchButton);
    }

    public void tapHotelDestinationField() {
        By hotelTo = By.xpath(
                "//android.widget.TextView[@text='TO']/following-sibling::android.widget.EditText"
              + " | //android.widget.EditText");
        click(hotelTo);
    }

    public void tapHotelSearchButton() {
        click(searchButton);
    }

    public boolean isBusTabVisible() {
        return isVisible(busTab, 10);
    }

    public boolean isBusTabVisible(int timeoutSeconds) {
        return isVisible(busTab, timeoutSeconds);
    }

    public boolean isHotelTabVisible() {
        return isVisible(hotelTab, 5);
    }
}
