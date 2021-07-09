package com.example.SpringMongoDB.controllers;

import com.example.SpringMongoDB.models.Customer;
import com.example.SpringMongoDB.repo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class Contoller {

    @Autowired
    Repository repo ;

    @GetMapping("/all")
    public List<Customer> getCustomers(){

        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getAnUserDetails(@PathVariable Integer id){
        if(!repo.existsById(id)){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND) ;
        }
        // You Can put pathVarable(value = "id") to specify varaible
        Customer customer = repo.findById(id).get();

        return new ResponseEntity<Customer>(customer,HttpStatus.CREATED);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Customer> CreateUser(@RequestBody Customer customer) {

        if(repo.existsById(customer.getId())){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND) ;
        }
        repo.insert(customer);
        return new ResponseEntity<Customer>(HttpStatus.OK) ;
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> updateEmployee(@RequestBody Customer customer , @PathVariable Integer id){

        if(repo.existsById(id)){

            Customer updatedCustomer = repo.findById(id).get();

            updatedCustomer.setFirstName(customer.getFirstName());
            updatedCustomer.setLastName(customer.getLastName());

            repo.save(customer);

            return new ResponseEntity<Customer>(HttpStatus.OK);

        }

            return  new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);


    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Customer> deleteEmployee(@PathVariable Integer id){

        if(repo.existsById(id)){

            repo.deleteById(id);
            return new ResponseEntity<Customer>(HttpStatus.OK) ;

        }
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND) ;

    }

}
