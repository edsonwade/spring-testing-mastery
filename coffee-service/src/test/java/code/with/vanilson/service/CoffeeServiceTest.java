package code.with.vanilson.service;

import code.with.vanilson.model.Coffee;
import code.with.vanilson.repository.CoffeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoffeeServiceTest {
    @Mock
    private CoffeeRepository repository;

    @InjectMocks
    private CoffeeService coffeeService;

    @Test
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
    }
}
