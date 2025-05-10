package com.finance.PersonalFinanceManager.controller;

import com.finance.PersonalFinanceManager.model.Customer;
import com.finance.PersonalFinanceManager.model.Transaction;
import com.finance.PersonalFinanceManager.service.CustomerService;
import com.finance.PersonalFinanceManager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.register(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customer) {
        try {
            Optional<Customer> loggedIn = customerService.login(customer.getUsername(), customer.getPassword());
            if (loggedIn.isPresent()) {
                return new ResponseEntity<>(loggedIn.get(), HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invalid credentials");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Server error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{customerId}/transaction")
    public String addTransaction(@PathVariable Long customerId, @RequestBody Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        try {
            transaction.setCustomerId(customerId);
            transaction.setDate(java.time.LocalDate.now());  // Set current date before saving
            transactionService.addTransaction(transaction);

            Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                double balance = customer.getBalance();
                double updatedBalance = transaction.getType().equals("credit") ? balance + transaction.getAmount() : balance - transaction.getAmount();
                customerService.updateBalance(customerId, updatedBalance);
            }
            return "Transaction added successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding transaction: " + e.getMessage();
        }
    }

    @GetMapping("/{customerId}/transactions")
    public List<Transaction> getTransactions(@PathVariable Long customerId) {
        return transactionService.getTransactionsByCustomer(customerId);
    }

    @GetMapping("/{customerId}/transactions/today")
    public List<Transaction> getTodayTransactions(@PathVariable Long customerId) {
        return transactionService.getTodayTransactions(customerId);
    }

    @GetMapping("/{customerId}/transactions/monthly")
    public String getMonthlyComparison(@PathVariable Long customerId,
                                       @RequestParam int year,
                                       @RequestParam int month) {
        YearMonth currentMonth = YearMonth.of(year, month);
        YearMonth lastMonth = currentMonth.minusMonths(1);

        double currentTotal = transactionService.getMonthlyTransactions(customerId, currentMonth)
                .stream().mapToDouble(Transaction::getAmount).sum();
        double lastTotal = transactionService.getMonthlyTransactions(customerId, lastMonth)
                .stream().mapToDouble(Transaction::getAmount).sum();

        if (currentTotal > lastTotal) {
            return "Warning: You spent more this month than last month!";
        } else {
            return "Good job! You've spent less this month compared to last.";
        }
    }
}
