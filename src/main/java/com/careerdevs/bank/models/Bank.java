package com.careerdevs.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//we're telling sql that id will be generated automatically
    private Long id;
    private String name;
    private String location;
    private String phoneNumber;


    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY) //If issues. change lazy to eager
//    @JsonIncludeProperties({"firstName", "lastName", "id"}) // - show only this or
    @JsonIgnoreProperties({"email", "age", "location", "bank"}) //show everything but this, blacklixt
    private List<Customer> customers;
    //Can use sets instead of lists


    //default constructor, we need to write it when we have another constructor:

    public Bank() {

    }

    //constructor
    public Bank(String name, String location, String phoneNumber) {
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    //getters:

    public List<Customer> getCustomers() {
        return customers;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAreaCode() {
        return phoneNumber.substring(0, 3);
    }


    //setters:


    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
