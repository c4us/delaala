package io.c4us.delaalaapi.repo;

import io.c4us.delaalaapi.domain.Car;
import io.c4us.delaalaapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepo extends JpaRepository<Car, String> {
    Optional<Car> findById(String id);

}
