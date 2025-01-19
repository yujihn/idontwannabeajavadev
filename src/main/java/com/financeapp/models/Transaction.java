package com.financeapp.models;

import java.time.LocalDateTime;

public class Transaction {
    private String category;
    private double amount;
    private String transactionType;
    private LocalDateTime timestamp;


    public Transaction() {
    }
    public Transaction(String category, double amount, String transactionType, LocalDateTime timestamp) {
        this.category = category;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
