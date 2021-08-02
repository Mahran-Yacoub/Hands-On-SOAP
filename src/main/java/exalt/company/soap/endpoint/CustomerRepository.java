package exalt.company.soap.endpoint;

import exalt.company.soap.spring_boot_soap.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This Interface will used to add any methods that you need to interact in db
 */
public interface CustomerRepository extends MongoRepository<Customer,Integer> {

}
