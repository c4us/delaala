package io.c4us.delaalaapi.repo;

import io.c4us.delaalaapi.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,String> {
    @Override
    Optional<Customer> findById(String id);
}
