package code.with.vanilson.bdd.steps;

import code.with.vanilson.bdd.CucumberSpringConfiguration;
import code.with.vanilson.model.Coffee;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shared Step Definitions for all Coffee Application features.
 * This class implements all steps from coffee_management.feature and coffee_retrieval.feature.
 * It follows SaaS-grade best practices by treating the system as a black box and interacting via REST API.
 *
 * @author vanilson
 * @version 1.0
 */
public class CoffeeSteps extends CucumberSpringConfiguration {

    private ResponseEntity<Coffee> lastCoffeeResponse;
    private ResponseEntity<List<Coffee>> lastCoffeeListResponse;
    private ResponseEntity<Void> lastVoidResponse;
    private Coffee lastCreatedCoffee;
    private Long lastUsedId;

    // --- SHARED GIVEN STEPS ---

    @Given("the coffee catalog is accessible")
    public void theCoffeeCatalogIsAccessible() {
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/coffees", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Given("a coffee with name {string} exists")
    public void aCoffeeWithNameExists(String name) {
        Coffee coffee = new Coffee(name);
        lastCoffeeResponse = restTemplate.postForEntity(getBaseUrl() + "/coffee", coffee, Coffee.class);
        assertThat(lastCoffeeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        lastCreatedCoffee = lastCoffeeResponse.getBody();
        lastUsedId = Objects.requireNonNull(lastCreatedCoffee).getId();
    }

    @Given("the following coffees exist in the catalog:")
    public void theFollowingCoffeesExistInTheCatalog(DataTable dataTable) {
        List<Map<String, String>> coffees = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : coffees) {
            String name = row.get("name");
            restTemplate.postForEntity(getBaseUrl() + "/coffee", new Coffee(name), Coffee.class);
        }
    }

    // --- WHEN STEPS (ACTIONS) ---

    @When("I create a new coffee with name {string}")
    public void iCreateANewCoffeeWithName(String name) {
        Coffee coffee = new Coffee(name);
        lastCoffeeResponse = restTemplate.postForEntity(getBaseUrl() + "/coffee", coffee, Coffee.class);
    }

    @When("I try to create a coffee with no name")
    public void iTryToCreateACoffeeWithNoName() {
        // Attempting to create a coffee with a null name to trigger validation
        Coffee coffee = new Coffee(null);
        lastCoffeeResponse = restTemplate.postForEntity(getBaseUrl() + "/coffee", coffee, Coffee.class);
    }

    @When("I change the name of {string} to {string}")
    public void iChangeTheNameOfTo(String oldName, String newName) {
        // We use the ID of the coffee created in the Given step
        Coffee update = new Coffee(newName);
        HttpHeaders headers = new HttpHeaders();
        // Concurrency control via ETag/If-Match
        headers.set("If-Match", String.valueOf(lastCreatedCoffee.getVersion()));
        HttpEntity<Coffee> entity = new HttpEntity<>(update, headers);

        lastCoffeeResponse = restTemplate.exchange(
                getBaseUrl() + "/coffee/" + lastUsedId,
                HttpMethod.PUT,
                entity,
                Coffee.class
        );
    }

    @When("I remove the coffee {string}")
    public void iRemoveTheCoffee(String name) {
        lastVoidResponse = restTemplate.exchange(
                getBaseUrl() + "/coffee/" + lastUsedId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }

    @When("I request a list of all coffees")
    public void iRequestAListOfAllCoffees() {
        lastCoffeeListResponse = restTemplate.exchange(
                getBaseUrl() + "/coffees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Coffee>>() {}
        );
    }

    @When("I request the details for that specific coffee")
    public void iRequestTheDetailsForThatSpecificCoffee() {
        lastCoffeeResponse = restTemplate.getForEntity(getBaseUrl() + "/coffee/" + lastUsedId, Coffee.class);
    }

    @When("I request details for a coffee that does not exist")
    public void iRequestDetailsForACoffeeThatDoesNotExist() {
        lastCoffeeResponse = restTemplate.getForEntity(getBaseUrl() + "/coffee/9999", Coffee.class);
    }

    // --- THEN STEPS (VERIFICATIONS) ---

    @Then("the coffee {string} should be successfully created")
    public void theCoffeeShouldBeSuccessfullyCreated(String name) {
        assertThat(lastCoffeeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(lastCoffeeResponse.getBody()).getName()).isEqualTo(name);
        lastCreatedCoffee = lastCoffeeResponse.getBody();
        lastUsedId = lastCreatedCoffee.getId();
    }

    @Then("the coffee should reflect the new name {string}")
    public void theCoffeeShouldReflectTheNewName(String name) {
        assertThat(lastCoffeeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(lastCoffeeResponse.getBody()).getName()).isEqualTo(name);
    }

    @Then("the coffee {string} should no longer be available in the catalog")
    public void theCoffeeShouldNoLongerBeAvailableInTheCatalog(String name) {
        assertThat(lastVoidResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Verify deletion by attempting to retrieve it
        ResponseEntity<Coffee> check = restTemplate.getForEntity(getBaseUrl() + "/coffee/" + lastUsedId, Coffee.class);
        assertThat(check.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Then("the operation should be rejected due to invalid data")
    public void theOperationShouldBeRejectedDueToInvalidData() {
        // Assert that the API returns a client error (400 Bad Request) due to validation failure
        assertThat(lastCoffeeResponse.getStatusCode().is4xxClientError())
                .as("Expected a 4xx error status for invalid data")
                .isTrue();
    }

    @Then("I should see {string} and {string} in the list")
    public void iShouldSeeAndInTheList(String name1, String name2) {
        assertThat(lastCoffeeListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Coffee> list = lastCoffeeListResponse.getBody();
        assertThat(list).extracting(Coffee::getName).contains(name1, name2);
    }

    @Then("I should receive the correct details for {string}")
    public void iShouldReceiveTheCorrectDetailsFor(String name) {
        assertThat(lastCoffeeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(lastCoffeeResponse.getBody()).getName()).isEqualTo(name);
    }

    @Then("I should be informed that the coffee was not found")
    public void iShouldBeInformedThatTheCoffeeWasNotFound() {
        assertThat(lastCoffeeResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @And("I should be able to retrieve {string} by its identifier")
    public void iShouldBeAbleToRetrieveByItsIdentifier(String name) {
        ResponseEntity<Coffee> response =
                restTemplate.getForEntity(getBaseUrl() + "/coffee/" + lastUsedId, Coffee.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(name);
    }
}
