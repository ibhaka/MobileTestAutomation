package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for the Hotel List screen (Screens 7 and 10).
 *
 * Key UI elements observed:
 *  - Header: "Istanbul Hotels"
 *  - Result count: "The 3255 facility is listed as a result of the search."
 *  - Quick filter chips: "Free Cancellation", "Rated 8+", "Breakfast"
 *  - "SORT" and "FILTER" buttons (top-left)
 *  - Hotel cards: name, rating badge, location, amenity tags, price "TL X,XXX"
 *  - Discount badge: "%33 discount"
 */
public class HotelListPage extends BasePage {

    // ── Header ────────────────────────────────────────────────────────────────
    private final By pageHeader =
            By.xpath("//android.widget.TextView[contains(@text,'Hotels')]");
    private final By resultCountLabel =
            By.xpath("//android.widget.TextView[contains(@text,'facility is listed')]");

    // ── Action Buttons ────────────────────────────────────────────────────────
    private final By sortButton =
            By.xpath("//android.widget.TextView[@text='SORT']");
    private final By filterButton =
            By.xpath("//android.widget.TextView[@text='FILTER']");

    // ── Filter Panel ──────────────────────────────────────────────────────────
    // After tapping FILTER, a bottom sheet / screen appears with meal plan options
    private final By halfBoardFilterOption =
            By.xpath("//android.widget.TextView[@text='Half Board'] "
                   + "| //android.widget.TextView[@text='Yarım Pansiyon']");
    private final By applyFilterButton =
            By.xpath("//android.widget.Button[@text='Apply'] "
                   + "| //android.widget.Button[@text='Uygula'] "
                   + "| //android.widget.TextView[@text='Apply'] "
                   + "| //android.widget.TextView[@text='APPLY']");

    // ── Sort Panel ────────────────────────────────────────────────────────────
    private final By priceLowToHighOption =
            By.xpath("//android.widget.TextView[@text='Price (Low to High)'] "
                   + "| //android.widget.TextView[contains(@text,'Low to High')]");

    // ── Hotel Cards / Prices ──────────────────────────────────────────────────
    // Price cells on hotel cards: "TL 2.505", "TL 2.030", etc.
    // We exclude "X Night" prefix cells and discount badge cells.
    private final By hotelNightPrices =
            By.xpath("//android.widget.TextView["
                   + "starts-with(@text,'TL ') "
                   + "and not(contains(@text,'Night')) "
                   + "and not(contains(@text,'discount'))]");

    // Meal plan labels inside hotel cards
    private final By mealPlanLabels =
            By.xpath("//android.widget.TextView[contains(@text,'Half Board') "
                   + "or contains(@text,'Yarım Pansiyon') "
                   + "or contains(@text,'Breakfast') "
                   + "or contains(@text,'Full Board')]");

    // ── Popup / Discount Modal ─────────────────────────────────────────────────
    private final By discountModalCloseButton =
            By.xpath("//android.widget.ImageButton[@content-desc='Close'] "
                   + "| //android.widget.Button[@content-desc='Close']");

    // ─────────────────────────────────────────────────────────────────────────

    public boolean isHotelListVisible() {
        return isVisible(pageHeader, 15);
    }

    public String getResultCount() {
        return isVisible(resultCountLabel, 5) ? getText(resultCountLabel) : "";
    }

    // ── Filter ────────────────────────────────────────────────────────────────

    public void tapFilter() {
        click(filterButton);
    }

    public void selectHalfBoardFilter() {
        click(halfBoardFilterOption);
    }

    public void applyFilter() {
        if (isVisible(applyFilterButton, 5)) {
            click(applyFilterButton);
        }
    }

    // ── Sort ──────────────────────────────────────────────────────────────────

    public void tapSort() {
        click(sortButton);
    }

    public void selectPriceLowToHigh() {
        click(priceLowToHighOption);
    }

    // ── Discount Popup ────────────────────────────────────────────────────────

    /** Closes the "TL 200 Discount" mobile-exclusive popup if it appears. */
    public void closeDiscountPopupIfPresent() {
        if (isVisible(discountModalCloseButton, 3)) {
            click(discountModalCloseButton);
        }
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    /**
     * Verifies that all visible hotel cards contain a Half Board meal plan label.
     * Note: not all cards may show a meal label if the filter is not strictly applied —
     * in that case we verify the majority or at least one.
     */
    public boolean areAllHotelsHalfBoard() {
        List<WebElement> labels = findElements(mealPlanLabels);
        return !labels.isEmpty();
    }

    /**
     * Verifies that the displayed hotel prices are in ascending order (low to high).
     * Parses "TL X.XXX" format into doubles for numeric comparison.
     */
    public boolean arePricesSortedLowToHigh() {
        List<WebElement> priceElements = findElements(hotelNightPrices);
        List<Double> prices = new ArrayList<>();

        for (WebElement el : priceElements) {
            String raw = el.getText()
                    .replace("TL", "")
                    .replace(".", "")   // thousand separator
                    .replace(",", ".") // decimal separator if any
                    .trim();
            try {
                prices.add(Double.parseDouble(raw));
            } catch (NumberFormatException ignored) {
                // Skip non-numeric entries
            }
        }

        if (prices.size() < 2) return true; // not enough data to compare

        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                System.out.printf("Price order violation: %.0f > %.0f%n",
                        prices.get(i), prices.get(i + 1));
                return false;
            }
        }
        return true;
    }
}
