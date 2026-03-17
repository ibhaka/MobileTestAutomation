# Obilet Appium Test Automation

Mobile test automation project for the [obilet.com](https://www.obilet.com) Android app.
Built with **Appium 3.x + Selenium Java + Cucumber BDD**.

<img width="1398" height="779" alt="Ekran Resmi 2026-03-18 01 23 14" src="https://github.com/user-attachments/assets/88d11dff-225d-4de8-be71-cab145e919c1" />


---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 17 |
| Appium Java Client | 9.1.0 |
| Selenium | 4.18.1 |
| Cucumber | 7.15.0 |
| JUnit | 4.13.2 |
| Extent Reports | 5.1.1 |
| Build Tool | Maven |

---

## Project Structure

```
obilet-appium/
├── pom.xml
└── src/
    ├── main/java/
    │   ├── pages/
    │   │   ├── BasePage.java
    │   │   ├── WelcomePage.java         # Screen 1-2: Welcome + notification dialog
    │   │   ├── HomePage.java            # Screen 3: Bus & Hotel search home
    │   │   ├── CitySearchPage.java      # City/destination search overlay
    │   │   ├── BusListPage.java         # Screen 4: Bus trip listing
    │   │   ├── SeatSelectionPage.java   # Screen 5: Seat map + selection
    │   │   ├── TicketPackagePage.java   # Ticket package popup (Premium/Prime)
    │   │   ├── PaymentPage.java         # Payment / order summary screen
    │   │   └── HotelListPage.java       # Screen 7/10: Hotel list + filter/sort
    │   └── utils/
    │       ├── DriverManager.java       # Appium driver lifecycle
    │       └── WaitUtils.java           # Explicit wait helpers
    └── test/
        ├── java/
        │   ├── runners/
        │   │   └── CucumberRunner.java
        │   └── steps/
        │       ├── AppiumHooks.java      # @Before / @After hooks
        │       ├── BusTicketSteps.java   # Case 1 step definitions
        │       └── HotelFilterSteps.java # Case 2 step definitions
        └── resources/
            ├── features/
            │   ├── bus_ticket.feature
            │   └── hotel_filter.feature
            ├── config/
            │   └── config.properties
            └── extent.properties
```

---

## Prerequisites

1. **Java 17** installed  
2. **Maven 3.8+** installed  
3. **Appium 3.x** installed globally:
   ```bash
   npm install -g appium
   appium driver install uiautomator2
   ```
4. **Android emulator** running with the obilet app installed:
   ```bash
   # Verify emulator is connected
   adb devices
   # Should show: emulator-5554   device
   ```
5. **obilet APK** already installed on the emulator (we installed it via adb install-multiple)

---

## Configuration

Edit `src/test/resources/config/config.properties`:

```properties
appium.url=http://127.0.0.1:4723
device.name=emulator-5554        # match your adb devices output
platform.version=13.0
app.package=com.obilet.androidside
app.activity=com.obilet.androidside.MainActivity
```

---

## Running Tests

### 1. Start Appium server (separate terminal)
```bash
appium
```

### 2. Verify emulator is running
```bash
adb devices
```

### 3. Run tests

```bash
# All @smoke tests (bus + hotel)
mvn clean test

# Only bus tests
mvn clean test -Dcucumber.filter.tags="@bus"

# Only hotel tests
mvn clean test -Dcucumber.filter.tags="@hotel"

# Full bus purchase + hotel filter scenario
mvn clean test -Dcucumber.filter.tags="@bus_purchase or @hotel_filter_sort"
```

---

## Test Cases

### Case 1 — Bus Ticket Purchase (`@bus_purchase`)
1. App opens → Welcome screen → Continue → Deny notifications
2. Bus tab selected on home screen
3. FROM: "Istanbul Europe" → TO: "Ankara" → SEARCH
4. First available trip selected (View button)
5. First empty seat selected on the seat map
6. "Confirm and Continue" tapped
7. Ticket package popup dismissed (Continue without package)
8. **Assertions on Payment page:**
   - Trip route contains "Istanbul" and "Ankara"
   - Total amount matches the price shown on seat selection screen
   - Selected seat number appears in "Passenger (Seat No: X)"

### Case 2 — Hotel Filter & Sort (`@hotel_filter_sort`)
1. App opens → Welcome screen → Continue → Deny notifications
2. Hotel tab selected on home screen
3. Destination: "Istanbul" → SEARCH
4. Hotel list displayed
5. FILTER → "Half Board" selected → Apply
6. **Assertion:** Filtered results contain Half Board meal plan
7. SORT → "Price (Low to High)"
8. **Assertion:** Displayed prices are in ascending numeric order

---

## Reports

After test run, reports are generated in:

| Report | Path |
|---|---|
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber.json` |
| Extent Spark | `target/extent-reports/SparkReport.html` |

Open the HTML report in a browser to see detailed step results with screenshots on failure.

---

## Seat Gender Rules

Per the task requirements, the obilet bus app enforces gender adjacency rules on 2+1 seated buses:
- A **Female** passenger cannot sit next to a **Male** passenger
- A **Male** passenger cannot sit next to a **Female** passenger

The app enforces this natively with a warning when an invalid seat is tapped.
The automation selects the **first available empty seat** — if the app shows a gender conflict warning,
the test will detect the absence of a confirmed seat selection and fail with a clear message.
# MobileTestAutomation
