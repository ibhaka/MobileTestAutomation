package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Main test runner for the Obilet Appium test suite.
 *
 * Tags:
 *   @smoke           → runs all smoke scenarios (bus + hotel)
 *   @bus             → runs only bus ticket scenarios
 *   @hotel           → runs only hotel scenarios
 *   @bus_purchase    → runs the main bus purchase scenario
 *   @hotel_filter_sort → runs the full filter + sort scenario
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
        },
        tags = "@smoke",
        monochrome = true
)
public class CucumberRunner {
}
