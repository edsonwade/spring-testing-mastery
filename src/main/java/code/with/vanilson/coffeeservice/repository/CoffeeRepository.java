package code.with.vanilson.coffeeservice.repository;

import java.util.List;

import code.with.vanilson.coffeeservice.model.Coffee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
    List<Coffee> findByName(String name);
}
