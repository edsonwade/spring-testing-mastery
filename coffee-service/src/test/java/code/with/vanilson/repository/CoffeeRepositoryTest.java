package code.with.vanilson.repository;

import code.with.vanilson.model.Coffee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Persistence layer tests for {@link CoffeeRepository}.
 * Uses DataJpaTest to test with an in-memory H2 database.
 *
 * @author vanilson
 * @version 1.0
 */
@DataJpaTest
public class CoffeeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CoffeeRepository coffeeRepository;

    /**
     * Maintains a list of the generated IDs for each coffee we preload in the database
     */
    private final List<Long> coffeeIds = new ArrayList<>();

    /**
     * The test coffees that we persist to the database before every test
     */
    private final List<Coffee> testCoffees = Arrays.asList(
            new Coffee("Coffee 1", 1),
            new Coffee("Coffee 2", 1),
            new Coffee("Coffee 3", 1));

    /**
     * Preloads test data before each test.
     */
    @BeforeEach
    void setUp() {
        // Load three coffees into the database
        testCoffees.forEach(coffee -> {
            // Save the coffee to the database
            entityManager.persist(coffee);

            // Add the generated ID to our list of coffee IDs
            coffeeIds.add((Long) entityManager.getId(coffee));
        });

        // Flush the persisted entities to the database
        entityManager.flush();
    }

    /**
     * Cleans up test data after each test.
     */
    @AfterEach
    void tearDown() {
        // Remove all coffees from the database
        coffeeIds.forEach(id -> coffeeRepository.deleteById(id));

        // Clear the list of generated Ids
        coffeeIds.clear();
    }

    /**
     * Test finding a coffee by its ID.
     */
    @Test
    void testFindCoffeeById() {
        // Find the coffee by ID
        Coffee foundCoffee = coffeeRepository.findById(
                coffeeIds.get(0)).orElse(null);

        // Validate that we found the coffee
        assertNotNull(foundCoffee);

        // Validate the coffee's fields
        assertEquals(coffeeIds.get(0), foundCoffee.getId());
        assertEquals("Coffee 1", foundCoffee.getName());
        assertEquals(1, foundCoffee.getVersion());
    }

    /**
     * Test finding a coffee by ID that does not exist.
     */
    @Test
    void testFindCoffeeByIdNotFound() {
        // Find the coffee by ID
        Coffee foundCoffee = coffeeRepository.findById(coffeeIds.get(0) + 4).orElse(null);

        // Validate that we found the coffee
        assertNull(foundCoffee);
    }

    /**
     * Test inserting a new coffee into the repository.
     */
    @Test
    void testInsertCoffee() {
        // Add a coffee to the database
        Coffee coffee = new Coffee("New Coffee", 2);
        Coffee insertedCoffee = coffeeRepository.save(coffee);

        // Validate the fields
        assertEquals("New Coffee", insertedCoffee.getName());
        assertEquals(2, insertedCoffee.getVersion());

        // Validate that the coffee is actually in the database
        Coffee foundCoffee = coffeeRepository.findById(insertedCoffee.getId()).orElse(null);
        assertNotNull(foundCoffee);
        assertEquals("New Coffee", foundCoffee.getName());
        assertEquals(2, foundCoffee.getVersion());

        // Find the coffee using the TestEntityManager
        Coffee foundCoffeeEM = entityManager.find(Coffee.class, insertedCoffee.getId());
        assertNotNull(foundCoffeeEM);
        assertEquals("New Coffee", foundCoffeeEM.getName());
        assertEquals(2, foundCoffeeEM.getVersion());
    }

    /**
     * Test updating an existing coffee.
     */
    @Test
    void testUpdateCoffee() {
        // Find the coffee to update
        Coffee coffee = coffeeRepository.findById(coffeeIds.get(0)).orElse(null);
        assertNotNull(coffee);

        // Update the coffee
        coffee.setName("Updated Coffee 1");
        coffee.setVersion(2);

        // Save the coffee to the database
        coffeeRepository.save(coffee);

        // Load the coffee from the database
        Coffee updatedCoffee = coffeeRepository.findById(coffee.getId()).orElse(null);
        assertNotNull(updatedCoffee);

        // Validate the fields
        assertEquals("Updated Coffee 1", updatedCoffee.getName());
        assertEquals(2, updatedCoffee.getVersion());
    }

    /**
     * Test finding all coffees in the repository.
     */
    @Test
    void testFindAll() {
        // Find all coffees
        List<Coffee> coffeeList = coffeeRepository.findAll();

        // Validate that we found 3 coffees
        assertEquals(3, coffeeList.size());
    }

    /**
     * Test deleting a coffee from the repository.
     */
    @Test
    void testDeleteCoffee() {
        // Find the first coffee and verify that it is found
        Coffee foundCoffee = coffeeRepository.findById(coffeeIds.get(0)).orElse(null);
        assertNotNull(foundCoffee);

        // Delete the coffee
        coffeeRepository.deleteById(foundCoffee.getId());

        // Validate that the coffee is no longer in the repository
        Optional<Coffee> notFoundCoffee = coffeeRepository.findById(foundCoffee.getId());
        assertFalse(notFoundCoffee.isPresent());
    }

    /**
     * Test finding coffees by name.
     */
    @Test
    void testFindCoffeeByName() {
        List<Coffee> found = coffeeRepository.findByName("Coffee 2");
        assertEquals(1, found.size(), "Expected to find one Coffee");

        Coffee foundCoffee = found.get(0);
        assertEquals("Coffee 2", foundCoffee.getName());
        assertEquals(1, foundCoffee.getVersion());
    }
}
