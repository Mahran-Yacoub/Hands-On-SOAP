package com.example.soap.endpoint;

import com.example.soap.spring_boot_soap.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repository extends MongoRepository<Customer,Integer> {

}
