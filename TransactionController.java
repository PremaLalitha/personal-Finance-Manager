package com.finance.PersonalFinanceManager.controller;

import com.finance.PersonalFinanceManager.model.Transaction;
import com.finance.PersonalFinanceManager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }

    @GetMapping("/all/{customerId}")
    public List<Transaction> getTransactionsByCustomer(@PathVariable Long customerId) {
        return transactionService.getTransactionsByCustomer(customerId);
    }

    @GetMapping("/today-totals/{customerId}")
    public TransactionService.Totals getTodayTotals(@PathVariable Long customerId) {
        return transactionService.getTodayTotals(customerId);
    }

    @GetMapping("/totals/{customerId}/{date}")
    public TransactionService.Totals getTotalsByDate(@PathVariable Long customerId, @PathVariable String date) {
        return transactionService.getTotalsByDate(customerId, java.time.LocalDate.parse(date));
    }
}
