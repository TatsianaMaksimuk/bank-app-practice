package com.careerdevs.bank.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//we're telling sql that id will be generated automatically
    private Long id;
    private String name;
    private String location;
    private String phoneNumber;


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
