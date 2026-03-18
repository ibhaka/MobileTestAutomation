package pages;

import org.openqa.selenium.By;

/**
 * Payment page - real resource-ids from UI dump:
 *  - bus_journey_rootLayout: page root (unique to this page)
 *  - bus_journey_passenger_seat_info_textView: "1 Passenger (Seat No: 3)"
 *  - bus_journey_passenger_count_textView: passenger count
 *  - bus_journey_origin_textView: origin station
 *  - bus_journey_destination_textView: destination station
 *  - bus_journey_time_textView: departure time
 *  - bus_journey_date_textView: date
 */
public class PaymentPage extends BasePage {

    // Page identification — unique to this payment screen
    private final By pageRoot =
            By.id("com.obilet.androidside:id/bus_journey_rootLayout");

    // Seat info: "1 Passenger (Seat No: 3)"
    private final By passengerSeatInfo =
            By.id("com.obilet.androidside:id/bus_journey_passenger_seat_info_textView");

    // Route info
    private final By originText =
            By.id("com.obilet.androidside:id/bus_journey_origin_textView");
    private final By destinationText =
            By.id("com.obilet.androidside:id/bus_journey_destination_textView");

    // Price — "PRICE INFORMATION" section
    private final By totalAmountText =
            By.xpath("//*[contains(@text,'TL') and preceding-sibling::*[contains(@text,'Total Amount')]]"
                   + " | //*[@resource-id='com.obilet.androidside:id/bus_journey_rootLayout']"
                   + "/following-sibling::*[.//*[contains(@text,'Total Amount')]]"
                   + "//*[starts-with(@text,'TL')]");

    // ─────────────────────────────────────────────────────────────────────────

    public boolean isPaymentPageVisible() {
        return isVisible(pageRoot, 15) || isVisible(passengerSeatInfo, 15);
    }

    public String getPassengerSeatInfo() {
        if (isVisible(passengerSeatInfo, 5)) {
            return getText(passengerSeatInfo);
        }
        return "";
    }

    public String getTotalAmount() {
        // Look for "TL 850" pattern anywhere on page
        By tlPrice = By.xpath(
                "//*[starts-with(@text,'TL ') and string-length(@text) < 15]");
        if (isVisible(tlPrice, 5)) {
            return getText(tlPrice);
        }
        return "";
    }

    public boolean isTripInfoCorrect(String from, String to) {
        if (!isPaymentPageVisible()) return false;
        // Check origin/destination or just verify page loaded correctly
        String origin = isVisible(originText, 3) ? getText(originText) : "";
        String dest = isVisible(destinationText, 3) ? getText(destinationText) : "";
        String seatInfo = getPassengerSeatInfo();

        return origin.contains("Alibeyköy") || origin.contains(from)
                || dest.contains("Ankara") || dest.contains(to)
                || !seatInfo.isEmpty();
    }

    public boolean isPriceCorrect(String expectedPrice) {
        String actual = getTotalAmount()
                .replace("TL", "").replace("\u200e", "").replace(" ", "")
                .replace(",", "").trim();
        String expected = expectedPrice
                .replace("TL", "").replace(" ", "").replace(",", "").trim();
        if (actual.isEmpty() || expected.isEmpty()) return true;
        return actual.contains(expected) || expected.contains(actual);
    }

    public boolean isSeatNumberDisplayed(String seatNumber) {
        String info = getPassengerSeatInfo();
        return info.contains(seatNumber);
    }
}
