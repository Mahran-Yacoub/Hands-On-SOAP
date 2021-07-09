package com.example.SpringMongoDB.repo;

import com.example.SpringMongoDB.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repository extends MongoRepository<Customer,Integer> {

}
