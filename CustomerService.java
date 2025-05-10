package com.finance.PersonalFinanceManager.service;

import com.finance.PersonalFinanceManager.model.Customer;
import com.finance.PersonalFinanceManager.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer register(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> login(String username, String password) {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        return customer.filter(c -> c.getPassword().equals(password));
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public void updateBalance(Long customerId, double newBalance) {
        customerRepository.findById(customerId).ifPresent(c -> {
            c.setBalance(newBalance);
            customerRepository.save(c);
        });
    }
}
