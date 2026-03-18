package pages;

import org.openqa.selenium.By;

public class WelcomePage extends BasePage {

    // Welcome screen
    private final By continueButton =
            By.xpath("//android.widget.Button[@text='Continue']");

    // Notification permission dialog — system dialog buttons
    private final By allowButton =
            By.xpath("//android.widget.Button[@text='Allow'] "
                   + "| //android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_button']");
    private final By dontAllowButton =
            By.xpath("//android.widget.Button[@text=\"Don't allow\"] "
                   + "| //android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_deny_button']");

    public boolean isWelcomeScreenVisible() {
        return isVisible(continueButton, 5);
    }

    public void tapContinue() {
        click(continueButton);
    }

    public void allowNotifications() {
        if (isVisible(allowButton, 5)) {
            click(allowButton);
        }
    }

    public void denyNotifications() {
        if (isVisible(dontAllowButton, 5)) {
            click(dontAllowButton);
        }
    }

    /** Handles both welcome screen and notification dialog in sequence. */
    public void handleWelcomeAndNotifications() {
        if (isWelcomeScreenVisible()) {
            tapContinue();
        }
        // Notification dialog may appear after continue
        denyNotifications();
    }
}
