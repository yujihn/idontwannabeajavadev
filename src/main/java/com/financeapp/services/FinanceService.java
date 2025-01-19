package com.financeapp.services;

import com.financeapp.models.Transaction;
import com.financeapp.models.User;
import com.financeapp.models.Wallet;
import com.financeapp.utils.BudgetExceededException;
import com.financeapp.utils.CategoryNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

public class FinanceService {
    public void addTransaction(Wallet wallet, String category, double amount, String transactionType) {
        Transaction transaction = new Transaction(category, amount, transactionType, LocalDateTime.now());
        wallet.addTransaction(transaction);
        if (transactionType.equals("income")){
            wallet.addBalance(amount);
        }
        else if(transactionType.equals("expense")){
            wallet.subtractBalance(amount);
        }
    }
    public void checkBudget(Wallet wallet, String category, double amount) throws CategoryNotFoundException, BudgetExceededException {
        if (wallet.getBudgets().containsKey(category)) {
            double budgetLimit = wallet.getBudgets().get(category);
            double currentExpenses =  wallet.getTransactions().stream()
                    .filter(transaction -> transaction.getCategory().equals(category) && transaction.getTransactionType().equals("expense"))
                    .mapToDouble(Transaction::getAmount).sum();
            if (currentExpenses + amount > budgetLimit) {
                throw new BudgetExceededException("Budget for category " + category + " exceeded!");
            }
        }
    }
    public void setBudget(Wallet wallet, String category, double limit) {
        wallet.addBudget(category, limit);
    }
    public double getTotalIncome(Wallet wallet) {
        return wallet.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType().equals("income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    public Map<String, Double> getIncomeByCategories(Wallet wallet) {
        Map<String, Double> incomes = new HashMap<>();
        for (Transaction transaction: wallet.getTransactions()){
            if (transaction.getTransactionType().equals("income")){
                incomes.compute(transaction.getCategory(), (key, value) -> (value == null ? 0 : value) + transaction.getAmount());
            }
        }
        return incomes;
    }
    public double getTotalExpense(Wallet wallet) {
        return wallet.getTransactions().stream()
                .filter(transaction -> transaction.getTransactionType().equals("expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    public Map<String, Map<String, Double>> getBudgetStatus(Wallet wallet) {
        Map<String, Map<String, Double>> budgetStatus = new HashMap<>();
        for (Map.Entry<String, Double> entry : wallet.getBudgets().entrySet()){
            String category = entry.getKey();
            double limit = entry.getValue();
            double currentExpenses =  wallet.getTransactions().stream()
                    .filter(transaction -> transaction.getCategory().equals(category) && transaction.getTransactionType().equals("expense"))
                    .mapToDouble(Transaction::getAmount).sum();
            double remaining = limit - currentExpenses;
            Map<String, Double> status = new HashMap<>();
            status.put("limit", limit);
            status.put("remaining", remaining);
            budgetStatus.put(category, status);
        }
        return budgetStatus;
    }
    public double getBalance(Wallet wallet) {
        return wallet.getBalance();
    }
    public double getTotalBySelectedCategories(Wallet wallet, List<String> categories, String transactionType) throws CategoryNotFoundException {
        double sum = 0;
        for (String category: categories){
            if(!wallet.getTransactions().stream().anyMatch(t-> t.getCategory().equals(category))){
                throw new CategoryNotFoundException("Category not found: " + category);
            }
            sum += wallet.getTransactions().stream()
                    .filter(transaction -> transaction.getCategory().equals(category) && transaction.getTransactionType().equals(transactionType))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
        }
        return sum;
    }
    public void transferBalance(User sender, User receiver, double amount) {
        addTransaction(sender.getWallet(), "transfer", amount, "expense");
        addTransaction(receiver.getWallet(), "gifted", amount, "income");
    }

}