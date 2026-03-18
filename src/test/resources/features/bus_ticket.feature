@bus
Feature: Bus Ticket Purchase
  As a user searching for intercity bus travel,
  I want to search for a bus trip, select a seat and proceed to payment,
  So that I can verify the correct trip details and price are shown on the payment page.

  Background:
    Given the user is on the bus home screen

  @smoke @bus_purchase
  Scenario: Search for a bus ticket and proceed to payment with correct trip details
    When the user searches for a bus from "Istanbul Europe" to "Ankara"
    And the user selects the first available trip
    And the user selects a seat
    And the user confirms the seat selection
    And the user dismisses the ticket package popup
    Then the payment page should be displayed
    And the trip route should include "Istanbul" and "Ankara"
    And the total price on the payment page should match the selected trip price
    And the selected seat number should be displayed on the payment page
