package com.careerdevs.bank.repositories;

import com.careerdevs.bank.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    //Because Name has a field in the model of Bank we can tell MySQL to query for it
    Optional<Bank> findByName(String name);

    @Query("SELECT b FROM Bank b WHERE b.phoneNumber LIKE ?1%") //1 stands in for our 1st var below
    List<Bank> findAllAreaCodes(String areaCode);
}
