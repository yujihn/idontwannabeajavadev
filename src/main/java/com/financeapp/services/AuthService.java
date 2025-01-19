package com.financeapp.services;

import com.financeapp.models.User;
import com.financeapp.utils.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public void registerUser(String username, String password) throws AuthenticationException {
        if (users.containsKey(username)){
            throw new AuthenticationException("User with username " + username + " already exists");
        }
        users.put(username, new User(username, password));
    }

    public User loginUser(String username, String password) throws AuthenticationException {
        User user = users.get(username);
        if (user== null){
            throw new AuthenticationException("User with username " + username + " not found");
        }
        if (!user.getPassword().equals(password)){
            throw new AuthenticationException("Incorrect password");
        }
        return user;
    }
    public Map<String, User> getAllUsers(){
        return  users;
    }
    public User getUserByUsername(String username){
        return users.get(username);
    }
}