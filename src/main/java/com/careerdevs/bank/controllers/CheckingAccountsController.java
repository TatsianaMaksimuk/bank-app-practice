package com.careerdevs.bank.controllers;

import com.careerdevs.bank.models.CheckingAccount;
import com.careerdevs.bank.models.Customer;
import com.careerdevs.bank.repositories.CheckingAccountRepository;
import com.careerdevs.bank.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/checking")
public class CheckingAccountsController {


    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/customer/{id}")
    public ResponseEntity<?> addOneCheckingAccountToDB (@RequestBody CheckingAccount newCheckingAccountData, @PathVariable Long id){

        //find customer in customerDB
        //return bad request if no customer:

        Customer requestedCustomer = customerRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        //add the customer data to the newCheckingAccount object
        newCheckingAccountData.getCustomers().add(requestedCustomer);
        //save it

        CheckingAccount addedCheckingAccount = checkingAccountRepository.save(newCheckingAccountData);
        return new ResponseEntity<>(addedCheckingAccount, HttpStatus.CREATED);
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllCheckingAccounts(){
        List<CheckingAccount> allCheckingAccounts = checkingAccountRepository.findAll();
        return new ResponseEntity<>(allCheckingAccounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCheckingAccountByID(@PathVariable Long id){
        CheckingAccount requestedCheckingAccount = checkingAccountRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(requestedCheckingAccount, HttpStatus.OK);
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> updateCheckingAccountByID(@PathVariable Long id, @RequestBody CheckingAccount newCheckingAccountData){
        CheckingAccount requestedCheckingAccount = checkingAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!newCheckingAccountData.getAlias().equals("")){
            requestedCheckingAccount.setAlias(newCheckingAccountData.getAlias());
        }
        return ResponseEntity.ok((checkingAccountRepository.save(requestedCheckingAccount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCheckingAccountByID(@PathVariable Long id){
        CheckingAccount requestedCheckingAccount = checkingAccountRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
       // checkingAccountRepository.delete(requestedCheckingAccount); //same:
        checkingAccountRepository.deleteById(id);
        return ResponseEntity.ok(requestedCheckingAccount);
    }

    @GetMapping ResponseEntity<?> getAccountsByBankId(@PathVariable Long id){
        return ResponseEntity.ok(checkingAccountRepository.findAllByCustomers_Bank_Id(id));
    }


    @PutMapping("/addCustomer/{cid}/{aId}")
    public ResponseEntity<?> addCustomerToAccount(@PathVariable Long cId, @PathVariable Long aId){
        // find account or return 404
        CheckingAccount requestedCheckingAccount = checkingAccountRepository.findById(aId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //find customer or return 404
        Customer requestedCustomer = customerRepository.findById(cId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //add customer to account's customer list
        requestedCheckingAccount.getCustomers().add(requestedCustomer);
        return ResponseEntity.ok(checkingAccountRepository.save(requestedCheckingAccount));
        //save
    }
}
