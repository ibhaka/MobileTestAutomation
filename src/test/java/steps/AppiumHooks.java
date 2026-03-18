package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import utils.DriverManager;

/**
 * Cucumber lifecycle hooks.
 * - @Before  : starts the Appium session before each scenario
 * - @After   : captures a screenshot on failure, then quits the driver
 */
public class AppiumHooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("\n─────────────────────────────────────────────────");
        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("─────────────────────────────────────────────────");
        DriverManager.getDriver(); // initialise driver
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = DriverManager.getDriver()
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "failure-screenshot");
                System.out.println("Screenshot attached for failed scenario: "
                        + scenario.getName());
            } catch (Exception e) {
                System.err.println("Could not capture screenshot: " + e.getMessage());
            }
        }
        System.out.println("Scenario [" + scenario.getStatus() + "]: "
                + scenario.getName());
        DriverManager.quitDriver();
    }
}
