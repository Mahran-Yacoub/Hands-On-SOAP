package com.example.soap.endpoint;

import com.example.soap.spring_boot_soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class CustomerEndPoint {

    @Autowired
    Repository repo ;

    @PayloadRoot(namespace = "http://soap.example.com/spring-boot-soap",
            localPart = "getAllRequest")
    @ResponsePayload
    public GetAllResponse getCustomers(@RequestPayload GetAllRequest request){

        GetAllResponse allCustomer = new GetAllResponse();
        List<Customer> allCustomers = repo.findAll() ;
        String allDetails = new String() ;

        for(Customer customer : allCustomers){

            allDetails+= "\n" + customer.toString();
        }

        allCustomer.setAllCustomer(allDetails);
        return allCustomer;
    }


    @PayloadRoot(namespace = "http://soap.example.com/spring-boot-soap",
            localPart = "getUserIDRequest")
    @ResponsePayload
    public GetUserResponse  getAnUserDetails(@RequestPayload GetUserIDRequest request){

        if(request.getStatus().equals("get")){

            return  getCustomer(request);

        }else{

            return  deleteCustomer(request);
        }
    }

    private GetUserResponse getCustomer(GetUserIDRequest request) {

        int id = request.getId() ;
        Customer customer = null;
        GetUserResponse response = new GetUserResponse();

        if(!repo.existsById(id)){

            response.setCustomer(customer);
            return response;
        }

        customer = repo.findById(id).get();
        response.setCustomer(customer);

        return response;

    }

    private GetUserResponse deleteCustomer(GetUserIDRequest request) {

        int id = request.getId() ;
        Customer customer = null;
        GetUserResponse response = new GetUserResponse();

        if(!repo.existsById(id)){

            response.setCustomer(customer);
            return response;
        }

        customer = repo.findById(id).get();
        repo.deleteById(id);
        response.setCustomer(customer);

        return response;

    }


    @PayloadRoot(namespace = "http://soap.example.com/spring-boot-soap",
            localPart = "customer")
    @ResponsePayload
    public GetMessageResponse CreateCustomerOrUpdate(@RequestPayload Customer customer) {

        if(customer.getStatus().equals("create")){

            return createCustomer(customer);

        }else if(customer.getStatus().equals("update")){

            return updateCustomer(customer);
        }

        GetMessageResponse response = new GetMessageResponse();
        response.setMessage("Enter create or update");
        return response ;
    }

    private GetMessageResponse updateCustomer(Customer customer) {

        int id = customer.getId();
        GetMessageResponse response = new GetMessageResponse();

        if (repo.existsById(id)) {

            Customer updatedCustomer = repo.findById(id).get();

            updatedCustomer.setFirstName(customer.getFirstName());
            updatedCustomer.setLastName(customer.getLastName());

            repo.save(updatedCustomer);
            response.setMessage("Done we Update it");

            return response;

        }

        response.setMessage("Filed to Update , Not Exist");
        return  response ;
    }

    private GetMessageResponse createCustomer(Customer customer) {

        int id = customer.getId();
        GetMessageResponse response = new GetMessageResponse();

        if (repo.existsById(id)) {

            response.setMessage("It is ALready Exist");
            return response;

        }

        response.setMessage("Done We Create it");
        repo.insert(customer);
        return  response ;
    }


}
