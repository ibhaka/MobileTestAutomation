package steps;

import io.cucumber.java.en.*;
import org.junit.Assert;
import pages.*;

public class BusTicketSteps {

    private final WelcomePage welcomePage        = new WelcomePage();
    private final HomePage homePage              = new HomePage();
    private final CitySearchPage citySearchPage  = new CitySearchPage();
    private final BusListPage busListPage        = new BusListPage();
    private final SeatSelectionPage seatPage     = new SeatSelectionPage();
    private final TicketPackagePage packagePage  = new TicketPackagePage();
    private final PaymentPage paymentPage        = new PaymentPage();

    private String selectedTotalPrice = "";
    private String selectedSeatNumber = "";

    @Given("the user is on the bus home screen")
    public void theUserIsOnTheBusHomeScreen() {
        // Handle welcome screen + notification dialog
        welcomePage.handleWelcomeAndNotifications();

        // Wait up to 30s for home screen after splash
        boolean busTabVisible = homePage.isBusTabVisible(30);

        // Second attempt if not visible yet
        if (!busTabVisible) {
            welcomePage.handleWelcomeAndNotifications();
            busTabVisible = homePage.isBusTabVisible(15);
        }

        Assert.assertTrue("Bus tab should be visible on home screen", busTabVisible);

        // Select Bus tab explicitly — ensures we are NOT on Flight/Hotel
        homePage.selectBusTab();
    }

    @When("the user searches for a bus from {string} to {string}")
    public void theUserSearchesForABus(String from, String to) {
        // FROM
        homePage.tapFromField();
        citySearchPage.typeCity(from);
        citySearchPage.selectCity(from);
        homePage.waitForHomeScreen();

        // TO
        homePage.tapToField();
        citySearchPage.typeCity(to);
        citySearchPage.selectCity(to);
        homePage.waitForHomeScreen();

        // Verify Bus tab is still selected before searching
        homePage.selectBusTab();
        homePage.tapBusSearchButton();
    }

    @When("the user selects the first available trip")
    public void theUserSelectsTheFirstAvailableTrip() {
        Assert.assertTrue("Bus list page should be visible", busListPage.isPageVisible());
        selectedTotalPrice = busListPage.selectFirstAvailableTripAndGetPrice();
        System.out.println("Selected trip price: " + selectedTotalPrice);
    }

    @When("the user selects a seat")
    public void theUserSelectsASeat() {
        Assert.assertTrue("Seat selection screen should be visible",
                seatPage.isSeatSelectionVisible());
        selectedSeatNumber = seatPage.selectFirstEmptySeat();
        System.out.println("Selected seat: " + selectedSeatNumber);
    }

    @When("the user confirms the seat selection")
    public void theUserConfirmsTheSeatSelection() {
        Assert.assertTrue("A seat should be selected", seatPage.isSeatSelected());
        selectedTotalPrice = seatPage.getTotalPrice();
        System.out.println("Total price: " + selectedTotalPrice);
        seatPage.tapConfirmAndContinue();
    }

    @When("the user dismisses the ticket package popup")
    public void theUserDismissesTheTicketPackagePopup() {
        if (packagePage.isPackageDialogVisible()) {
            System.out.println("Package base price: " + packagePage.getBasePrice());
            packagePage.continueWithoutSelectingPackage();
        }
    }

    @Then("the payment page should be displayed")
    public void thePaymentPageShouldBeDisplayed() {
        Assert.assertTrue("Payment page should be visible",
                paymentPage.isPaymentPageVisible());
    }

    @Then("the trip route should include {string} and {string}")
    public void theTripRouteShouldInclude(String from, String to) {
        Assert.assertTrue("Trip route should contain correct cities",
                paymentPage.isTripInfoCorrect(from, to));
    }

    @Then("the total price on the payment page should match the selected trip price")
    public void theTotalPriceShouldMatchSelectedTripPrice() {
        if (!selectedTotalPrice.isEmpty()) {
            Assert.assertTrue(
                    "Price mismatch. Expected: " + selectedTotalPrice
                    + ", Actual: " + paymentPage.getTotalAmount(),
                    paymentPage.isPriceCorrect(selectedTotalPrice));
        } else {
            Assert.assertFalse("Total amount should not be empty",
                    paymentPage.getTotalAmount().isEmpty());
        }
    }

    @Then("the selected seat number should be displayed on the payment page")
    public void theSelectedSeatNumberShouldBeDisplayedOnPaymentPage() {
        if (!selectedSeatNumber.isEmpty()) {
            Assert.assertTrue("Seat number should appear on payment page",
                    paymentPage.isSeatNumberDisplayed(selectedSeatNumber));
        }
    }
}
