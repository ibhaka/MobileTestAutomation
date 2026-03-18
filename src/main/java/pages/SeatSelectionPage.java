package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Seat Selection screen.
 *
 * Resource-ids from UI dump:
 *  - select_seat_recyclerView: seat grid container
 *  - item_seat_first_column_textView: single seats (left)
 *  - item_seat_second_column_textView: double seats (right)
 *  - item_seat_fifth_column_textView: back row seats
 *  - gender_male_textview: "MALE" button in popup
 *  - gender_female_textview: "FEMALE" button in popup
 *  - select_seat_confirm_button: "Confirm and Continue"
 *  - select_seat_selection_info_total_price_textView: price after selection
 *  - seat_selection_info_currency_for_eng_textView: "TL" currency
 */
public class SeatSelectionPage extends BasePage {

    // Legend — confirms seat selection page
    private final By emptySeatLegend =
            By.id("com.obilet.androidside:id/select_seat_info_empty_textView");

    // Seat grid
    private final By seatRecycler =
            By.id("com.obilet.androidside:id/select_seat_recyclerView");

    // Seat columns
    private final By seatColumn1 =
            By.id("com.obilet.androidside:id/item_seat_first_column_textView");
    private final By seatColumn2 =
            By.id("com.obilet.androidside:id/item_seat_second_column_textView");
    private final By seatColumn5 =
            By.id("com.obilet.androidside:id/item_seat_fifth_column_textView");

    // Gender popup buttons
    private final By maleButton =
            By.id("com.obilet.androidside:id/gender_male_textview");
    private final By femaleButton =
            By.id("com.obilet.androidside:id/gender_female_textview");

    // Confirm and Continue button
    private final By confirmButton =
            By.id("com.obilet.androidside:id/select_seat_confirm_button");

    // Price after seat selected
    private final By totalPriceTextView =
            By.id("com.obilet.androidside:id/select_seat_selection_info_total_price_textView");
    private final By currencyLabel =
            By.id("com.obilet.androidside:id/seat_selection_info_currency_for_eng_textView");

    // ─────────────────────────────────────────────────────────────────────────

    public boolean isSeatSelectionVisible() {
        return isVisible(emptySeatLegend, 15) || isVisible(seatRecycler, 15);
    }

    /**
     * Selects the first empty seat and chooses Male gender from popup.
     * Returns the seat number.
     */
    public String selectFirstEmptySeat() {
        String seatNum = trySeatColumn(seatColumn1);
        if (seatNum != null) return seatNum;

        seatNum = trySeatColumn(seatColumn2);
        if (seatNum != null) return seatNum;

        seatNum = trySeatColumn(seatColumn5);
        if (seatNum != null) return seatNum;

        throw new RuntimeException("No empty seat found on seat selection screen.");
    }

    private String trySeatColumn(By columnLocator) {
        List<WebElement> seats = findElements(columnLocator);
        for (WebElement seat : seats) {
            try {
                String text = seat.getText().trim();
                if (!text.isEmpty() && text.matches("\\d+")) {
                    seat.click();
                    // Wait for gender popup to appear
                    if (isVisible(maleButton, 3)) {
                        click(maleButton);
                        System.out.println("Selected MALE gender for seat " + text);
                    } else if (isVisible(femaleButton, 3)) {
                        click(femaleButton);
                        System.out.println("Selected FEMALE gender for seat " + text);
                    }
                    // Wait for seat to be confirmed (price appears)
                    try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                    // Verify seat was actually selected (price visible)
                    if (isVisible(totalPriceTextView, 3)) {
                        return text;
                    }
                }
            } catch (Exception e) {
                System.out.println("Seat click failed: " + e.getMessage());
            }
        }
        return null;
    }

    public boolean isSeatSelected() {
        return isVisible(totalPriceTextView, 5);
    }

    public String getTotalPrice() {
        try {
            String currency = isVisible(currencyLabel, 3) ? getText(currencyLabel) : "TL";
            String price = isVisible(totalPriceTextView, 3) ? getText(totalPriceTextView) : "";
            return (currency + " " + price).trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void tapConfirmAndContinue() {
        click(confirmButton);
    }
}
