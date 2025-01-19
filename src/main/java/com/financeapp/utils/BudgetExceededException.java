package com.financeapp.utils;

public class BudgetExceededException extends Exception {
    public BudgetExceededException(String message) {
        super(message);
    }
}