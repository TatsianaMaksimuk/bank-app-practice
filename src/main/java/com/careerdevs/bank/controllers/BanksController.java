package com.careerdevs.bank.controllers;


import com.careerdevs.bank.models.Bank;
import com.careerdevs.bank.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin //for frontend
@RestController
@RequestMapping("/api/banks") //good practice for controllers that return data, not webpage
public class BanksController {

    @Autowired
    private BankRepository bankRepository;


    @PostMapping
    public ResponseEntity<?> addOneBankToDB(@RequestBody Bank newBankData) {
        try {
            //validation occurs here
            Bank addedBank = bankRepository.save(newBankData);
            //return ResponseEntity.ok(addedBank);//200 - success
            return new ResponseEntity<>(addedBank, HttpStatus.CREATED); // 201 code
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage()); // 500 error code
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBanksFromDB() {
        List<Bank> allBanks = bankRepository.findAll();
        //Same thing, 3 options:
        //return new ResponseEntity<>(allBanks, HttpStatus.OK);
        //return ResponseEntity.ok(bankRepository.findAll());//200 - success
        return new ResponseEntity<>(bankRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBankByID(@PathVariable Long id) {
//       Bank requestedBank = bankRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); - use it instead of try catch, autosends 404;

        Optional<Bank> requestedBank = bankRepository.findById(id);
        if (requestedBank.isEmpty()) {
            return new ResponseEntity<>("Bank not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requestedBank.get(), HttpStatus.OK);
    }
}
