package com.finance.PersonalFinanceManager.service;

import com.finance.PersonalFinanceManager.model.Transaction;
import org.springframework.transaction.annotation.Transactional;
import com.finance.PersonalFinanceManager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    public Transaction addTransaction(Transaction transaction) {
        System.out.println("Saving transaction: " + transaction);
        if (transaction.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is null");
        }
        // Additional validation can be added here
        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            System.out.println("Transaction saved successfully: " + savedTransaction);
            return savedTransaction;
        } catch (Exception e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            throw e;
        }
    }

    public List<Transaction> getTransactionsByCustomer(Long customerId) {
        System.out.println("Fetching transactions for customerId: " + customerId);
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        System.out.println("Number of transactions found: " + transactions.size());
        return transactions;
    }

    public List<Transaction> getTodayTransactions(Long customerId) {
        return transactionRepository.findByCustomerIdAndDate(customerId, LocalDate.now());
    }

    public List<Transaction> getMonthlyTransactions(Long customerId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return transactionRepository.findByCustomerIdAndDateBetween(customerId, start, end);
    }

    public Totals getTodayTotals(Long customerId) {
        List<Transaction> todayTransactions = getTodayTransactions(customerId);
        double totalIncome = 0.0;
        double totalSpending = 0.0;
        for (Transaction t : todayTransactions) {
            if ("credit".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("debit".equalsIgnoreCase(t.getType())) {
                totalSpending += t.getAmount();
            }
        }
        return new Totals(totalIncome, totalSpending);
    }

    public Totals getTotalsByDate(Long customerId, LocalDate date) {
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDate(customerId, date);
        double totalIncome = 0.0;
        double totalSpending = 0.0;
        for (Transaction t : transactions) {
            if ("credit".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("debit".equalsIgnoreCase(t.getType())) {
                totalSpending += t.getAmount();
            }
        }
        return new Totals(totalIncome, totalSpending);
    }

    public Map<String, Object> getMonthlyComparison(Long customerId) {
        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);

        Totals thisMonthTotals = getTotalsForMonth(customerId, currentMonth);
        Totals lastMonthTotals = getTotalsForMonth(customerId, lastMonth);

        Map<String, Object> comparison = new HashMap<>();
        comparison.put("thisMonthIncome", thisMonthTotals.getTotalIncome());
        comparison.put("thisMonthSpending", thisMonthTotals.getTotalSpending());
        comparison.put("lastMonthIncome", lastMonthTotals.getTotalIncome());
        comparison.put("lastMonthSpending", lastMonthTotals.getTotalSpending());

        String warning = "";
        if (thisMonthTotals.getTotalSpending() > lastMonthTotals.getTotalSpending()) {
            warning += "Warning: Your spending has increased compared to last month. ";
        }
        if (thisMonthTotals.getTotalIncome() < lastMonthTotals.getTotalIncome()) {
            warning += "Warning: Your income has decreased compared to last month.";
        }
        comparison.put("warningMessage", warning.trim());

        return comparison;
    }

    private Totals getTotalsForMonth(Long customerId, YearMonth month) {
        List<Transaction> transactions = getMonthlyTransactions(customerId, month);
        double totalIncome = 0.0;
        double totalSpending = 0.0;
        for (Transaction t : transactions) {
            if ("credit".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("debit".equalsIgnoreCase(t.getType())) {
                totalSpending += t.getAmount();
            }
        }
        return new Totals(totalIncome, totalSpending);
    }

    public static class Totals {
        private double totalIncome;
        private double totalSpending;

        public Totals(double totalIncome, double totalSpending) {
            this.totalIncome = totalIncome;
            this.totalSpending = totalSpending;
        }

        public double getTotalIncome() {
            return totalIncome;
        }

        public double getTotalSpending() {
            return totalSpending;
        }
    }
}
