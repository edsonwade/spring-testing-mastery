package code.with.vanilson.service;

import code.with.vanilson.model.Coffee;
import code.with.vanilson.repository.CoffeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CoffeeService}.
 * Tests all scenarios for the business logic.
 *
 * @author vanilson
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CoffeeServiceTest {

    @Mock
    private CoffeeRepository repository;

    @InjectMocks
    private CoffeeService coffeeService;

    /**
     * Test finding a coffee by its ID.
     */
    @Test
    @DisplayName("Should return coffee when found by ID")
    void testFindById() {
        Coffee coffee = new Coffee("My Coffee");
        coffee.setId(1L);
        coffee.setVersion(1);

        when(repository.findById(1L)).thenReturn(Optional.of(coffee));

        Optional<Coffee> c = coffeeService.findById(1L);
        assertTrue(c.isPresent());
        assertEquals(1L, c.get().getId());
        assertEquals("My Coffee", c.get().getName());
        assertEquals(1, c.get().getVersion());
        verify(repository, times(1)).findById(1L);
    }

    /**
     * Test finding all coffees.
     */
    @Test
    @DisplayName("Should return all coffees")
    void testFindAll() {
        List<Coffee> coffees = List.of(new Coffee("Espresso"), new Coffee("Latte"));
        when(repository.findAll()).thenReturn(coffees);

        List<Coffee> result = coffeeService.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    /**
     * Test creating a new coffee.
     */
    @Test
    @DisplayName("Should create a new coffee with version 1")
    void testCreate() {
        Coffee coffee = new Coffee("Cappuccino");
        Coffee savedCoffee = new Coffee("Cappuccino");
        savedCoffee.setId(1L);
        savedCoffee.setVersion(1);

        when(repository.save(any(Coffee.class))).thenReturn(savedCoffee);

        Coffee result = coffeeService.create(coffee);

        assertNotNull(result.getId());
        assertEquals(1, result.getVersion());
        verify(repository, times(1)).save(coffee);
    }

    /**
     * Test saving a coffee (update scenario).
     */
    @Test
    @DisplayName("Should save coffee")
    void testSave() {
        Coffee coffee = new Coffee("Mocha", 2);
        when(repository.save(coffee)).thenReturn(coffee);

        Coffee result = coffeeService.save(coffee);

        assertEquals("Mocha", result.getName());
        assertEquals(2, result.getVersion());
        verify(repository, times(1)).save(coffee);
    }

    /**
     * Test deleting a coffee by its ID.
     */
    @Test
    @DisplayName("Should delete coffee by ID")
    void testDeleteById() {
        doNothing().when(repository).deleteById(1L);

        coffeeService.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
