package com.financeapp;

import com.financeapp.models.ExpenseCategory;
import com.financeapp.models.IncomeCategory;
import com.financeapp.models.User;
import com.financeapp.services.AuthService;
import com.financeapp.services.FileService;
import com.financeapp.services.FinanceService;
import com.financeapp.utils.AuthenticationException;
import com.financeapp.utils.BudgetExceededException;
import com.financeapp.utils.CategoryNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static User currentUser;
    private static final AuthService authService = new AuthService();
    private static final FinanceService financeService = new FinanceService();
    private static final FileService fileService = new FileService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadUsers();
        while (true) {
            showLoginRegistrationMenu();
            String input = scanner.nextLine();
            if (!input.isEmpty()){
                try {
                    int choice = Integer.parseInt(input);
                    switch (choice) {
                        case 1:
                            login();
                            if (currentUser != null) {
                                financeManager();
                            }
                            break;
                        case 2:
                            register();
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                } catch (NumberFormatException e){
                    System.out.println("Invalid input. Please enter a number");
                }
            }
            else {
                System.out.println("Invalid input. Please enter a number");
            }
        }
    }

    private static void showLoginRegistrationMenu() {
        System.out.println("\nLogin/Registration Menu");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private static void register() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            authService.registerUser(username, password);
            saveUsers();
            System.out.println("Registration successful! Please log in.");
        } catch (AuthenticationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            currentUser = authService.loginUser(username, password);
            String filename = currentUser.getUsername() + "_wallet.json";
            fileService.loadWalletData(currentUser, filename);
            System.out.println("Login successful! Welcome, " + currentUser.getUsername());
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        String filename = "users.json";
        Map<String, User> loadedUsers = fileService.loadUserData(filename);
        loadedUsers.forEach((username, user) -> {
            try {
                authService.registerUser(username, user.getPassword());
            } catch (AuthenticationException e) {
                System.err.println("Error loading users " + e.getMessage());
            }
        });
    }

    private static void saveUsers() {
        String filename = "users.json";
        fileService.saveUserData(authService.getAllUsers(), filename);
    }


    private static void financeManager() {
        while (true) {
            showFinanceManagerMenu();
            String input = scanner.nextLine();
            if(!input.isEmpty()){
                try {
                    int choice = Integer.parseInt(input);
                    switch (choice) {
                        case 1:
                            addIncome();
                            break;
                        case 2:
                            addExpense();
                            break;
                        case 3:
                            setBudget();
                            break;
                        case 4:
                            showSummary();
                            break;
                        case 5:
                            calculateBySelectedCategories();
                            break;
                        case 6:
                            saveAndExit();
                            return;
                        case 7:
                            System.out.println("Exiting without saving.");
                            return;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                }  catch (NumberFormatException e){
                    System.out.println("Invalid input. Please enter a number");
                }
            } else {
                System.out.println("Invalid input. Please enter a number");
            }
        }
    }

    private static void showFinanceManagerMenu() {
        System.out.println("\n--- Finance Manager ---");
        System.out.println("1. Add income");
        System.out.println("2. Add expense");
        System.out.println("3. Set budget");
        System.out.println("4. Show summary");
        System.out.println("5. Calculate by selected categories");
        System.out.println("6. Save and exit");
        System.out.println("7. Exit without saving");
        System.out.print("Choose an option: ");
    }

    private static void addIncome() {
        try {
            System.out.print("Enter income category (available categories: " +
                    Arrays.stream(IncomeCategory.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")) + "): ");
            String categoryInput = scanner.nextLine().toUpperCase();
            IncomeCategory category = IncomeCategory.valueOf(categoryInput);
            double amount;
            try {
                System.out.print("Enter income amount: ");
                amount = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount");
                return;
            }
            financeService.addTransaction(currentUser.getWallet(), category.name(), amount, "income");
            System.out.println("Income added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category. Please choose from the list.");
        }
    }

    private static void addExpense() {
        try {
            System.out.print("Enter expense category (available categories: " +
                    Arrays.stream(ExpenseCategory.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")) + "): ");
            String categoryInput = scanner.nextLine().toUpperCase();
            ExpenseCategory category = ExpenseCategory.valueOf(categoryInput);
            if (category == ExpenseCategory.TRANSFER) {
                transferBalance();
                return;
            }

            double amount;
            try {
                System.out.print("Enter expense amount: ");
                amount = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount");
                return;
            }

            financeService.checkBudget(currentUser.getWallet(), category.name(), amount);
            financeService.addTransaction(currentUser.getWallet(), category.name(), amount, "expense");
            System.out.println("Expense added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category. Please choose from the list.");
        } catch (CategoryNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (BudgetExceededException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void setBudget() {
        try {
            System.out.print("Enter budget category (available categories: " +
                    Arrays.stream(ExpenseCategory.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")) + "): ");
            String categoryInput = scanner.nextLine().toUpperCase();
            ExpenseCategory category = ExpenseCategory.valueOf(categoryInput);
            if (category == ExpenseCategory.TRANSFER) {
                transferBalance();
                return;
            }
            double limit;
            try {
                System.out.print("Enter budget limit: ");
                limit = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid limit");
                return;
            }
            financeService.setBudget(currentUser.getWallet(), category.name(), limit);
            System.out.println("Budget set successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category. Please choose from the list.");
        }
    }

    private static void showSummary() {
        System.out.println("\n--- Summary ---");
        double totalIncome = financeService.getTotalIncome(currentUser.getWallet());
        System.out.println("Total income: " + totalIncome);

        Map<String, Double> incomesByCategories = financeService.getIncomeByCategories(currentUser.getWallet());
        System.out.println("Incomes by categories:");
        for (Map.Entry<String, Double> entry : incomesByCategories.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }

        double totalExpense = financeService.getTotalExpense(currentUser.getWallet());
        System.out.println("Total expenses: " + totalExpense);

        System.out.println("Budget status:");
        Map<String, Map<String, Double>> budgetStatus = financeService.getBudgetStatus(currentUser.getWallet());
        for (Map.Entry<String, Map<String, Double>> entry : budgetStatus.entrySet()) {
            System.out.println("- " + entry.getKey() + ": Limit: " + entry.getValue().get("limit") + ", Remaining: " + entry.getValue().get("remaining"));
        }

        double balance = financeService.getBalance(currentUser.getWallet());
        System.out.println("Balance: " + balance);

        if (totalExpense > totalIncome) {
            System.out.println("Warning: Expenses exceed income!");
        }
    }

    private static void calculateBySelectedCategories() {
        try {
            System.out.print("Enter categories separated by comma (e.g., FOOD,TRANSPORT): ");
            String categoriesInput = scanner.nextLine().toUpperCase();
            List<String> categories = Arrays.stream(categoriesInput.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            System.out.print("Enter transaction type ('income' or 'expense'): ");
            String transactionType = scanner.nextLine();
            if (!transactionType.equals("income") && !transactionType.equals("expense")) {
                System.out.println("Invalid transaction type.");
                return;
            }
            double total = financeService.getTotalBySelectedCategories(currentUser.getWallet(), categories, transactionType);
            System.out.println("Total " + transactionType + " for selected categories: " + total);
        } catch (CategoryNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void transferBalance(){

        System.out.println("Enter user to transfer to: ");
        String usernameToTransfer = scanner.nextLine();
        User userToTransfer = authService.getUserByUsername(usernameToTransfer);
        if(userToTransfer==null){
            System.out.println("User with username " + usernameToTransfer + " not found");
            return;
        }
        double amount;
        try {
            System.out.println("Enter amount to transfer: ");
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Invalid amount");
            return;
        }
        if (amount > currentUser.getWallet().getBalance()){
            System.out.println("Not enough funds");
            return;
        }
        financeService.transferBalance(currentUser, userToTransfer, amount);
        System.out.println("Transfer successful.");


    }


    private static void saveAndExit() {
        String filename = currentUser.getUsername() + "_wallet.json";
        fileService.saveWalletData(currentUser, filename);
        saveUsers();
        System.out.println("Data saved. Exiting...");
    }
}