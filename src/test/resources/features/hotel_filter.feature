@hotel
Feature: Hotel Filtering and Sorting
  As a user looking for accommodation,
  I want to filter hotel results by meal plan and sort them by price,
  So that I can quickly find the most affordable Half Board hotels.

  Background:
    Given the user is on the hotel home screen

  @smoke @hotel_filter_sort
  Scenario: Filter hotels by Half Board and sort by price low to high
    When the user searches for hotels in "Istanbul"
    Then the hotel list page should be visible
    When the user filters hotels by "Half Board"
    Then the hotel results should be filtered by "Half Board"
    When the user sorts hotels by "Price (Low to High)"
    Then the hotel results should be sorted by price from low to high

  @hotel_sort_only
  Scenario: Sort hotels by price low to high without filter
    When the user searches for hotels in "Istanbul"
    Then the hotel list page should be visible
    When the user sorts hotels by "Price (Low to High)"
    Then the hotel results should be sorted by price from low to high
