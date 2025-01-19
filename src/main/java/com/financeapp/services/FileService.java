package com.financeapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.financeapp.models.Transaction;
import com.financeapp.models.User;
import com.financeapp.models.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public FileService(){
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public void saveUserData(Map<String, User> users, String filename) {
        try {
            objectMapper.writeValue(new File(filename), users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public Map<String, User> loadUserData(String filename) {
        File file = new File(filename);
        Map<String, User> users = new HashMap<>();
        if (file.exists()) {
            try {
                users = objectMapper.readValue(file, new TypeReference<Map<String, User>>() {});
            } catch (IOException e) {
                System.err.println("Error loading users: " + e.getMessage());
            }
        }
        return users;
    }
    public void saveWalletData(User user, String filename) {
        try {
            Wallet wallet = user.getWallet();
            Map<String, Object> data = new HashMap<>();
            data.put("balance", wallet.getBalance());
            List<Map<String,Object>> transactions = wallet.getTransactions().stream().map(this::transactionToMap).toList();
            data.put("transactions", transactions);
            data.put("budgets", wallet.getBudgets());

            objectMapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            System.err.println("Error saving " + e.getMessage());
        }
    }

    private Map<String, Object> transactionToMap(Transaction transaction) {
        Map<String,Object> map = new HashMap<>();
        map.put("category", transaction.getCategory());
        map.put("amount", transaction.getAmount());
        map.put("transactionType", transaction.getTransactionType());
        map.put("timestamp", transaction.getTimestamp().toString());
        return map;
    }

    public void loadWalletData(User user, String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                Map<String, Object> data = objectMapper.readValue(file, HashMap.class);
                Wallet wallet = new Wallet();
                wallet.setBalance((Double) data.get("balance"));
                List<Map<String,Object>> transactionsData = (List<Map<String, Object>>) data.get("transactions");
                if (transactionsData!= null){
                    List<Transaction> transactions = transactionsData.stream().map(this::mapToTransaction).toList();
                    wallet.setTransactions(new ArrayList<>(transactions));
                }
                Map<String, Double> budgets = (Map<String, Double>) data.get("budgets");
                if(budgets!= null){
                    wallet.setBudgets(budgets);
                }

                user.setWallet(wallet);

            } catch (IOException e) {
                System.err.println("Error loading " + e.getMessage());
            }
        } else {
            System.out.println("No saved data for this user yet.");
        }
    }

    private Transaction mapToTransaction(Map<String, Object> transactionData){
        Transaction transaction = new Transaction();
        transaction.setCategory((String) transactionData.get("category"));
        transaction.setAmount((Double) transactionData.get("amount"));
        transaction.setTransactionType((String) transactionData.get("transactionType"));
        transaction.setTimestamp(java.time.LocalDateTime.parse((String) transactionData.get("timestamp")));
        return transaction;

    }
}