package code.with.vanilson.coffeeservice.service;

import java.util.List;
import java.util.Optional;

import code.with.vanilson.coffeeservice.model.Coffee;
import code.with.vanilson.coffeeservice.repository.CoffeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {this.coffeeRepository = coffeeRepository;}

    public List<Coffee> findAll() {
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
