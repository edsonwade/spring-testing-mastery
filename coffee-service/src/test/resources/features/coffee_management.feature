Feature: Coffee Management
  As a coffee shop manager
  I want to manage the coffee catalog
  So that customers can see the available options

  Scenario: Successfully create a new coffee
    Given the coffee catalog is accessible
    When I create a new coffee with name "Espresso"
    Then the coffee "Espresso" should be successfully created
    And I should be able to retrieve "Espresso" by its identifier

  Scenario: Update an existing coffee name
    Given a coffee with name "Latte" exists
    When I change the name of "Latte" to "Latte Macchiato"
    Then the coffee should reflect the new name "Latte Macchiato"

  Scenario: Delete a coffee from the catalog
    Given a coffee with name "Cappuccino" exists
    When I remove the coffee "Cappuccino"
    Then the coffee "Cappuccino" should no longer be available in the catalog

  Scenario: Attempt to create a coffee with invalid data
    Given the coffee catalog is accessible
    When I try to create a coffee with no name
    Then the operation should be rejected due to invalid data
