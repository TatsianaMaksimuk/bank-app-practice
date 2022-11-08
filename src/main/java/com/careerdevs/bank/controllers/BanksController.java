package com.careerdevs.bank.controllers;


import com.careerdevs.bank.models.Bank;
import com.careerdevs.bank.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
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

    @PostMapping("/{id}")
    public ResponseEntity<?> postOneBankById(@PathVariable Long id, @RequestBody Bank newBankData) {
        Bank requestedBank = bankRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Option 1 trusting no interceptor will modify data in the middle
        //       return ResponseEntity.ok(bankRepository.save(newBankData)); //newBankData should contain ALL fields

        //Option2 Allows us only change data provided to us, validates data at the same time, only changes data we allow to be changed
        if (!newBankData.getName().equals("")) { //Key must exist
            requestedBank.setName(newBankData.getName());
        }
        if (newBankData.getPhoneNumber() != null && newBankData.getPhoneNumber().length()>=3) {//key does not need to exist, if it does then cannot be empty
            requestedBank.setPhoneNumber(newBankData.getPhoneNumber());
        }
        return ResponseEntity.ok(bankRepository.save(requestedBank));
    }

    //Don't ever put delete All

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBankById(@PathVariable Long id) {
        //If return is unwanted, the below line is negligible - deleteById only fails if void is provided and that cannot be due to path.
        Bank requestedBank = bankRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        bankRepository.deleteById(id);
        return ResponseEntity.ok(requestedBank);
    }


    //Creating custom query
    @GetMapping("/name/{name}") //adding change to route, otherwise it will try to pass it as ID
    public ResponseEntity<?> findBankByName(@PathVariable String name) {
        Bank requestedBank = bankRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return new ResponseEntity<>(requestedBank, HttpStatus.OK);
    }

    @GetMapping("/areaCode/{areaCode}")
    public ResponseEntity<?> findAllBanksByAreaCode(@PathVariable String areaCode){
        return new ResponseEntity<>(bankRepository.findAllAreaCodes(areaCode), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> findBankByCustomerId(@PathVariable Long id){
        Bank foundBank = bankRepository.getByCustomers_id(id).orElseThrow(()-> new ResponseStatusException((HttpStatus.NOT_FOUND)));
        return new ResponseEntity<>(foundBank, HttpStatus.OK);
    }
 }
