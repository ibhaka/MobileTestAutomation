package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Bus Listing screen.
 *
 * Key resource-ids from UI dump:
 *  - Bus card root:    com.obilet.androidside:id/item_bus_journey_list_rootLayout
 *  - Bus card price:   com.obilet.androidside:id/item_price_textView
 *  - "View" button:    com.obilet.androidside:id/bus_journey_review_button_textview (text="View")
 *  - "Set an Alert":   com.obilet.androidside:id/bus_journey_review_button_textview (text="Set an Alert")
 *  - Next day button:  com.obilet.androidside:id/next_day_button
 *  - "Affordable Flights" section has id/item_journey_title_textView — NOT a bus card
 */
public class BusListPage extends BasePage {

    // Page identification — SORT button always visible on bus list
    private final By sortButton =
            By.id("com.obilet.androidside:id/quick_filter_order_main_textView");

    // Bus trip cards — identified by their root layout ID
    // "Affordable Flights" does NOT have this ID
    private final By busCardRoots =
            By.id("com.obilet.androidside:id/item_bus_journey_list_rootLayout");

    // Price inside bus card
    private final By busPriceTextView =
            By.id("com.obilet.androidside:id/item_price_textView");

    // "View" or "Set an Alert" button inside bus card
    // Both use the same resource-id, different text
    private final By busReviewButton =
            By.id("com.obilet.androidside:id/bus_journey_review_button_textview");

    // Next day navigation
    private final By nextDayButton =
            By.id("com.obilet.androidside:id/next_day_button");

    // ─────────────────────────────────────────────────────────────────────────

    public boolean isPageVisible() {
        return isVisible(sortButton, 20);
    }

    /**
     * Finds first available trip (View button) across multiple days.
     * Falls back to clicking first bus card directly if all occupied.
     */
    public String selectFirstAvailableTripAndGetPrice() {
        for (int day = 0; day < 5; day++) {
            // Look for "View" buttons inside bus cards only
            By viewInBusCard = By.xpath(
                "//*[@resource-id='com.obilet.androidside:id/bus_journey_review_button_textview' "
                + "and @text='View']");

            if (isVisible(viewInBusCard, 3)) {
                List<WebElement> views = findElements(viewInBusCard);
                if (!views.isEmpty()) {
                    String price = getPriceFromCard(views.get(0));
                    views.get(0).click();
                    return price;
                }
            }

            System.out.println("Day " + day + ": no available trips, going to next day...");
            if (isVisible(nextDayButton, 3)) {
                click(nextDayButton);
                try { Thread.sleep(2500); } catch (InterruptedException ignored) {}
            } else {
                break;
            }
        }

        // Fallback: click first bus card's CardView parent directly
        // Bus cards have item_bus_journey_list_rootLayout — Affordable Flights does NOT
        System.out.println("All trips full — clicking first bus card directly");
        List<WebElement> cards = findElements(busCardRoots);
        if (!cards.isEmpty()) {
            // Get price first
            List<WebElement> prices = findElements(busPriceTextView);
            String price = prices.isEmpty() ? "" : prices.get(0).getText();
            // Click the clickable CardView parent
            try {
                WebElement cardView = cards.get(0).findElement(
                    By.xpath("./parent::androidx.cardview.widget.CardView"));
                cardView.click();
            } catch (Exception e) {
                cards.get(0).click();
            }
            return price;
        }

        throw new RuntimeException("No bus trip cards found on the listing page.");
    }

    private String getPriceFromCard(WebElement buttonElement) {
        try {
            WebElement card = buttonElement.findElement(By.xpath(
                "./ancestor::*[@resource-id='com.obilet.androidside:id/item_bus_journey_list_rootLayout']"));
            WebElement price = card.findElement(By.id(
                "com.obilet.androidside:id/item_price_textView"));
            return "TL " + price.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
