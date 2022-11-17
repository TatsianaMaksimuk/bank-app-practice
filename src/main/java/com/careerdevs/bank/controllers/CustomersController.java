package com.careerdevs.bank.controllers;

import com.careerdevs.bank.models.Bank;
import com.careerdevs.bank.models.Customer;
import com.careerdevs.bank.models.User;
import com.careerdevs.bank.repositories.BankRepository;
import com.careerdevs.bank.repositories.CustomerRepository;
import com.careerdevs.bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    BankRepository bankRepository; //to check bank id we need to give access to bank repository

    @Autowired
    UserRepository userRepository;


    @PostMapping("/{bankId}") // creating customer into bank
    public ResponseEntity<?> createCustomer(@RequestBody Customer newCustomerData, @PathVariable Long bankId) {
        //Before we safe customerData, we need to find a bank by id in repository
        //if bank does not exist return bad request
        //if bank exists - add new customer data and save:

        Bank requestedBank = bankRepository.findById(bankId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)); //user got us bad request - data was wrong

        newCustomerData.setBank(requestedBank);
        Customer addedCustomer = customerRepository.save(newCustomerData);

        return new ResponseEntity<>(addedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCustomersFromDB() {
        List<Customer> allCustomers = customerRepository.findAll();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer requestedCustomer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(requestedCustomer, HttpStatus.OK);
    }


    @PutMapping("/{id}") //put should never create
    public ResponseEntity<?> updateOneCustomerById(@PathVariable Long id, @RequestBody Customer newCustomerData) {
        Customer requestedCustomer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!newCustomerData.getFirstName().equals("")) {
            requestedCustomer.setFirstName(newCustomerData.getFirstName());
        }
        if (!newCustomerData.getLastName().equals("")) {
            requestedCustomer.setLastName(newCustomerData.getLastName());
        }
        if (!newCustomerData.getEmail().equals("")) {
            requestedCustomer.setEmail(newCustomerData.getEmail());
        }
        if (!newCustomerData.getAge().equals("")) {
            requestedCustomer.setAge(newCustomerData.getAge());
        }
        if (!newCustomerData.getLocation().equals("")) {
            requestedCustomer.setLocation(newCustomerData.getLocation());
        }


        return ResponseEntity.ok(customerRepository.save(requestedCustomer));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Customer requestedCustomer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        customerRepository.deleteById(id);
        return new ResponseEntity<>(requestedCustomer, HttpStatus.OK);

    }

    @GetMapping("/name/{lastName}")
    public ResponseEntity<?> getCustomersByLastName(@PathVariable String lastName){
            List<Customer> foundCustomers = customerRepository.findAllByLastName(lastName);
            return new ResponseEntity<>(foundCustomers, HttpStatus.OK);
    }


    @GetMapping("/bank/{bankId}")
    public ResponseEntity<?> getAllCustomersByBankId(@PathVariable Long bankId){
        List<Customer> allCustomers = customerRepository.findAllByBank_id(bankId);
        return  new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }

    @PostMapping("/token/{loginToken}/{customerId}") //finding user and attaching it to customer// we store user inside a customer
    public ResponseEntity<?> getUserByLoginToken(@PathVariable String loginToken, @PathVariable Long customerId){
        //fnd user
        User requestedUser = userRepository.findUserByLoginToken(loginToken).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //find customer
        Customer requestedCustomer = customerRepository.findById(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        requestedCustomer.setUser(requestedUser);

        customerRepository.save(requestedCustomer);
        return new ResponseEntity<>(requestedCustomer,HttpStatus.OK);

    }

    @GetMapping("/self/{loginToken}")
    public ResponseEntity<?> getSelfByLoginToken(@PathVariable String loginToken) {
        User requestedUser = userRepository.findUserByLoginToken(loginToken).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var custs =customerRepository.findByUser_username(requestedUser.getUsername());
        Customer requestedCustomer = customerRepository.findByUser_username(requestedUser.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(requestedCustomer, HttpStatus.OK);
    }
}
