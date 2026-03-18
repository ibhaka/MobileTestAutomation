package steps;

import io.cucumber.java.en.*;
import org.junit.Assert;
import pages.*;

/**
 * Step definitions for Case 2: Hotel Filtering and Sorting.
 *
 * Flow:
 *  1. Welcome screen → Continue → Deny notifications
 *  2. Home screen → Hotel tab
 *  3. Enter destination → SEARCH
 *  4. Hotel list page → FILTER → "Half Board" → Apply
 *  5. Verify all results show Half Board
 *  6. SORT → "Price (Low to High)"
 *  7. Verify prices are sorted ascending
 */
public class HotelFilterSteps {

    private final WelcomePage welcomePage       = new WelcomePage();
    private final HomePage homePage             = new HomePage();
    private final CitySearchPage citySearchPage = new CitySearchPage();
    private final HotelListPage hotelListPage   = new HotelListPage();

    // ── Given ──────────────────────────────────────────────────────────────────

    @Given("the user is on the hotel home screen")
    public void theUserIsOnTheHotelHomeScreen() {
        if (welcomePage.isWelcomeScreenVisible()) {
            welcomePage.tapContinue();
            welcomePage.denyNotifications();
        }
        Assert.assertTrue("Hotel tab should be visible",
                homePage.isHotelTabVisible());
        homePage.selectHotelTab();
    }

    // ── When ───────────────────────────────────────────────────────────────────

    @When("the user searches for hotels in {string}")
    public void theUserSearchesForHotelsIn(String location) {
        homePage.tapHotelDestinationField();
        citySearchPage.typeCity(location);
        citySearchPage.selectFirstResult();
        homePage.tapHotelSearchButton();
    }

    @When("the user filters hotels by {string}")
    public void theUserFiltersHotelsBy(String filterOption) {
        Assert.assertTrue("Hotel list should be visible before filtering",
                hotelListPage.isHotelListVisible());
        System.out.println("Hotel results before filter: " + hotelListPage.getResultCount());

        hotelListPage.tapFilter();
        // Half Board / Yarım Pansiyon
        if (filterOption.equalsIgnoreCase("Half Board")
                || filterOption.equalsIgnoreCase("Yarım Pansiyon")) {
            hotelListPage.selectHalfBoardFilter();
        }
        hotelListPage.applyFilter();
    }

    @When("the user sorts hotels by {string}")
    public void theUserSortsHotelsBy(String sortOption) {
        hotelListPage.tapSort();
        if (sortOption.equalsIgnoreCase("Price (Low to High)")) {
            hotelListPage.selectPriceLowToHigh();
        }
    }

    // ── Then ───────────────────────────────────────────────────────────────────

    @Then("the hotel list page should be visible")
    public void theHotelListPageShouldBeVisible() {
        Assert.assertTrue("Hotel list page should be visible",
                hotelListPage.isHotelListVisible());
        System.out.println("Hotel results: " + hotelListPage.getResultCount());
    }

    @Then("the hotel results should be filtered by {string}")
    public void theHotelResultsShouldBeFilteredBy(String mealPlan) {
        System.out.println("Verifying filter: " + mealPlan);
        Assert.assertTrue(
                "Hotel list should contain results filtered by " + mealPlan,
                hotelListPage.areAllHotelsHalfBoard());
    }

    @Then("the hotel results should be sorted by price from low to high")
    public void theHotelResultsShouldBeSortedByPriceLowToHigh() {
        // Close any discount popup that might be blocking the list
        hotelListPage.closeDiscountPopupIfPresent();

        Assert.assertTrue(
                "Hotel results should be sorted by price from low to high",
                hotelListPage.arePricesSortedLowToHigh());
    }
}
