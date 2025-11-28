package code.with.vanilson.web;

import code.with.vanilson.model.Coffee;
import code.with.vanilson.service.CoffeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {this.coffeeService = coffeeService;}

    @GetMapping("/coffee/{id}")
    public ResponseEntity<?> getCoffee(@PathVariable Long id) {
        return coffeeService.findById(id)
                .map(coffee -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/coffee/" + id))
                                .eTag(Integer.toString(coffee.getVersion()))
                                .body(coffee);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/coffees")
    public ResponseEntity<List<Coffee>> getCoffees() {
        return ResponseEntity.ok().body(coffeeService.findAll());
    }

    @PostMapping("/coffee")
    public ResponseEntity<Coffee> createCoffee(@RequestBody Coffee coffee) {
        Coffee newCoffee = coffeeService.create(coffee);
        try {
            return ResponseEntity
                    .created(new URI("/coffee/" + newCoffee.getId()))
                    .eTag(Integer.toString(newCoffee.getVersion()))
                    .body(newCoffee);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/coffee/{id}")
    public ResponseEntity<?> updateCoffee(@RequestBody Coffee coffee,
                                          @PathVariable Long id,
                                          @RequestHeader("If-Match") Integer ifMatch) {
        Optional<Coffee> existingCoffee = coffeeService.findById(id);
        return existingCoffee.map(c -> {
            if (c.getVersion() != ifMatch) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Update the coffee
            c.setName(coffee.getName());
            c.setVersion(c.getVersion() + 1);

            Coffee updatedCoffee = coffeeService.save(c);
            try {
                return ResponseEntity.ok()
                        .location(new URI("/coffee/" + updatedCoffee.getId()))
                        .eTag(Integer.toString(updatedCoffee.getVersion()))
                        .body(c);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/coffee/{id}")
    public ResponseEntity<?> deleteCoffee(@PathVariable Long id) {
        // Get the existing product
        Optional<Coffee> existingCoffee = coffeeService.findById(id);

        return existingCoffee.map(coffee -> {
            coffeeService.deleteById(coffee.getId());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
