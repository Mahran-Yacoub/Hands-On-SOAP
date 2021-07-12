package com.example.soap.endpoint;

import com.example.soap.spring_boot_soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * This Class Will Be EndPoint For SOAP Request
 */
@Endpoint
public class CustomerEndPoint {

    @Autowired
    Repository repositoryDB;

    /**
     * This method will receive A SOAP request To Get All Customers details
     *
     * @param request GetAllRequest To Get All Customers Details
     *
     * @return All Customers Details That are storing in DB as String separated each one by new line
     */
    @PayloadRoot(namespace = "http://soap.example.com/spring-boot-soap",
            localPart = "getAllRequest")
    @ResponsePayload
    public GetAllResponse getCustomers(@RequestPayload GetAllRequest request) {

        GetAllResponse allCustomer = new GetAllResponse();
        List<Customer> allCustomers = repositoryDB.findAll();
        String allDetails = "";

        for (Customer customer : allCustomers) {
            allDetails += "\n" + customer.toString();
        }

        allCustomer.setAllCustomer(allDetails);
        return allCustomer;
    }


    /**
     * This method will receive a SOAP request To Get or delete
     * a specific Customer
     *
     * @param request GetOrDeleteCustomerIDRequest to get ID for a Customer
     *                and status of request is get (status == 1) or delete (status == 2)
     *
     * @return Customer details that deleted 0r Get
     */
    @PayloadRoot(namespace = "http://soap.example.com/spring-boot-soap",
            localPart = "getOrDeleteCustomerIDRequest")
    @ResponsePayload
    public Customer getOrDeleteCustomer(@RequestPayload GetOrDeleteCustomerIDRequest request) {
        int id = request.getId();
        int status = request.getStatus();

        if (status == 1) {

            return getCustomer(id);

        } else if (status == 2) {

            return deleteCustomer(id);

        } else {
            //Empty Customer Object when user enter some thing else than 1 or 2
            return new Customer();
        }
    }

    /**
     * This method will used To Get A Customer Details Using ID
     *
     * @param id ID Of Customer That We Need To Get His Details
     * @return Customer Object that contains Full Details About searched Customer Or
     * Empty Customer If He Is Not Exist.
     */
    private Customer getCustomer(int id) {

        if (!repositoryDB.existsById(id)) {

            return new Customer();
        }

        Customer customer = repositoryDB.findById(id).get();

        return customer;

    }

    /**
     * This Method Will Used To Delete A Customer Details Using ID
     *
     * @param id ID Of Customer That We Need To Delete His Details
     *
     * @return Customer Object That Contains Full Details About Deleted Customer or
     * Empty Customer if He Is Not Exist.
     */
    private Customer deleteCustomer(int id) {

        if (!repositoryDB.existsById(id)) {
            return new Customer();
        }

        Customer deletedCustomer = repositoryDB.findById(id).get();
        repositoryDB.deleteById(id);

        return deletedCustomer;

    }


    /**
     * This Method Will Be Used To Update A Customer Or Create New Customer
     *
     * @param request UpdateOrCreateRequest That Contains Customer Details that we need
     *                to Create New One Or Update Exist One And Status To Know
     *                Create(status == 1) Or Update (status == 2)
     *
     * @return Customer Full Details About Created or Updated Customer Or Empty Customer
     * Object if An Error Does Happen like There is not Customer to Update.
     */
    @PayloadRoot(namespace = "http://soap.example.com/spring-boot-soap",
            localPart = "updateOrCreateRequest")
    @ResponsePayload
    public Customer createOrUpdateCustomer(@RequestPayload UpdateOrCreateRequest request) {

        int status = request.getStatus();
        Customer customer = request.getCustomerDetails();

        if (status == 1) {

            return createCustomer(customer);

        } else if (status == 2) {

            return updateCustomer(customer);

        } else {

            return new Customer();
        }
    }

    /**
     * This Method will Use to Update Customer that are already exist
     *
     * @param customer Customer Details that we want to update for
     *
     * @return Customer Details After Updated Or Empty Customer Object If
     * A Customer That was entered is not exist.
     */
    private Customer updateCustomer(Customer customer) {

        int id = customer.getId();

        if (!repositoryDB.existsById(id)) {
            return new Customer();
        }

        Customer updatedCustomer = repositoryDB.findById(id).get();
        updatedCustomer.setFirstName(customer.getFirstName());
        updatedCustomer.setLastName(customer.getLastName());

        repositoryDB.save(updatedCustomer);

        return updatedCustomer;
    }

    /**
     * This method is used to create new Customer in DataBase
     *
     * @param customer Customer Details that we need to Create in DB
     *
     * @return Customer Details For New Customer in DataBase or Empty Customer
     * Object if he is already exist.
     */
    private Customer createCustomer(Customer customer) {

        int id = customer.getId();

        if (repositoryDB.existsById(id)) {
            return new Customer();
        }

        repositoryDB.insert(customer);
        return customer;
    }
}
