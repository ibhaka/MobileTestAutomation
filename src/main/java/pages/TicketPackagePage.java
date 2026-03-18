package pages;

import org.openqa.selenium.By;

/**
 * Ticket Package popup that appears after seat confirmation.
 * Shows Premium / Prime / Continue without package options.
 *
 * From screenshots: "TICKET PACKAGES" title, Premium TL 1,193,
 * Prime TL 1,150, "Continue without selecting a package (TL 1,050.00) >"
 */
public class TicketPackagePage extends BasePage {

    // Dialog title
    private final By dialogTitle =
            By.xpath("//android.widget.TextView[@text='TICKET PACKAGES']");

    // Continue without package link
    private final By continueWithoutPackage =
            By.xpath("//android.widget.TextView[contains(@text,'Continue without selecting a package')]");

    // ─────────────────────────────────────────────────────────────────────────

    public boolean isPackageDialogVisible() {
        return isVisible(dialogTitle, 5);
    }

    public void continueWithoutSelectingPackage() {
        click(continueWithoutPackage);
    }

    public String getBasePrice() {
        if (isVisible(continueWithoutPackage, 3)) {
            String text = getText(continueWithoutPackage);
            int start = text.indexOf("(TL ");
            int end = text.indexOf(")", start);
            if (start >= 0 && end > start) {
                return text.substring(start + 1, end).trim();
            }
        }
        return "";
    }
}
