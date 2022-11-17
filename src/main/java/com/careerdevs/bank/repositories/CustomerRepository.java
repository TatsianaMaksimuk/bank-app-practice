package com.careerdevs.bank.repositories;

import com.careerdevs.bank.models.Customer;
import com.careerdevs.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
   List <Customer> findAllByLastName(String lastName);

    List<Customer> findAllByBank_id(Long id);

    Optional<Customer> findByUser_username(String username);
}
