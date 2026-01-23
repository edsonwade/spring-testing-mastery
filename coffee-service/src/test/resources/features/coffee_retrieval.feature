Feature: Coffee Retrieval
  As a customer
  I want to browse and view coffee details
  So that I can decide what to order

  Scenario: List all available coffees
    Given the following coffees exist in the catalog:
      | name      |
      | Americano |
      | Cortado   |
    When I request a list of all coffees
    Then I should see "Americano" and "Cortado" in the list

  Scenario: Retrieve a coffee by its ID
    Given a coffee with name "Mocha" exists
    When I request the details for that specific coffee
    Then I should receive the correct details for "Mocha"

  Scenario: Handle non-existent coffee retrieval
    Given the coffee catalog is accessible
    When I request details for a coffee that does not exist
    Then I should be informed that the coffee was not found
