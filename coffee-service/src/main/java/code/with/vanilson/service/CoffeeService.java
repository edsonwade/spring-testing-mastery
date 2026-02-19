package code.with.vanilson.service;

import code.with.vanilson.model.Coffee;
import code.with.vanilson.repository.CoffeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {this.coffeeRepository = coffeeRepository;}

    public List<Coffee> findAllCoffees() {
        return coffeeRepository.findAll();
    }

    public Optional<Coffee> findById(Long id) {
        return coffeeRepository.findById(id);
    }

    public Coffee create(Coffee coffee) {
        coffee.setVersion(1);
        return coffeeRepository.save(coffee);
    }

    public Coffee save(Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    public void deleteById(Long id) {
        coffeeRepository.deleteById(id);
    }
}
