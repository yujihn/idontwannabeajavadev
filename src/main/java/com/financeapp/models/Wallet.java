package com.financeapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {
    private List<Transaction> transactions;
    private Map<String, Double> budgets;
    private double balance;

    public Wallet() {
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
        this.balance = 0;
    }
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void addBudget(String category, double limit){
        this.budgets.put(category, limit);
    }
    public void addBalance(double amount) {
        this.balance += amount;
    }
    public void subtractBalance(double amount) {
        this.balance -= amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Map<String, Double> getBudgets() {
        return budgets;
    }

    public void setBudgets(Map<String, Double> budgets) {
        this.budgets = budgets;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}