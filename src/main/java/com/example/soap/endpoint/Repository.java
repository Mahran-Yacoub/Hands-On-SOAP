package com.example.soap.endpoint;

import com.example.soap.spring_boot_soap.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This Interface will used to add any methods that you need to interact in db
 */
public interface Repository extends MongoRepository<Customer,Integer> {

}
