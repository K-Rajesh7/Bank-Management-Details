package BankProject;

import java.util.*;

class User {
    private int userId;
    private String username;
    private String password;

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Account {
    private int accountId;
    private int userId;
    private double balance;

    public Account(int accountId, int userId, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}


class Transaction {
    private int transactionId;
    private int accountId;
    private double amount;
    private String transactionType;
    private Date timestamp;

    public Transaction(int transactionId, int accountId, double amount, String transactionType) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = new Date();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

class BankingSystem {
    private Map<String, User> users = new HashMap<>();
    private Map<Integer, Account> accounts = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private int userIdCounter = 1;
    private int accountIdCounter = 1;
    private int transactionIdCounter = 1;

    public User registerUser(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        User user = new User(userIdCounter++, username, password);
        users.put(username, user);
        return user;
    }

    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }

    public Account createAccount(int userId) {
        Account account = new Account(accountIdCounter++, userId, 0.0);
        accounts.put(account.getAccountId(), account);
        return account;
    }

    public void deposit(int accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            transactions.add(new Transaction(transactionIdCounter++, accountId, amount, "DEPOSIT"));
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }

    public void withdraw(int accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            transactions.add(new Transaction(transactionIdCounter++, accountId, -amount, "WITHDRAW"));
        } else {
            throw new IllegalArgumentException("Insufficient balance or account not found.");
        }
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {
        Account fromAccount = accounts.get(fromAccountId);
        Account toAccount = accounts.get(toAccountId);
        if (fromAccount != null && toAccount != null && fromAccount.getBalance() >= amount) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            transactions.add(new Transaction(transactionIdCounter++, fromAccountId, -amount, "TRANSFER"));
            transactions.add(new Transaction(transactionIdCounter++, toAccountId, amount, "TRANSFER"));
        } else {
            throw new IllegalArgumentException("Transfer failed: check accounts and balance.");
        }
    }

    public List<Transaction> getTransactionHistory(int accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    public double checkBalance(int accountId) {
        Account account = accounts.get(accountId);
        if (account != null) {
            return account.getBalance();
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }
}

public class Bank {
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    try {
                        currentUser = bankingSystem.registerUser(username, password);
                        System.out.println("Registration successful.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    password = scanner.nextLine();
                    try {
                        currentUser = bankingSystem.loginUser(username, password);
                        System.out.println("Login successful.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

            if (currentUser != null) {
                while (true) {
                    System.out.println("1. Create Account");
                    System.out.println("2. Deposit");
                    System.out.println("3. Withdraw");
                    System.out.println("4. Transfer");
                    System.out.println("5. Check Balance");
                    System.out.println("6. Transaction History");
                    System.out.println("7. Logout");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 1:
                            Account account = bankingSystem.createAccount(currentUser.getUserId());
                            System.out.println("Account created. Account ID: " + account.getAccountId());
                            break;
                        case 2:
                            System.out.print("Enter account ID: ");
                            int accountId = scanner.nextInt();
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            try {
                                bankingSystem.deposit(accountId, depositAmount);
                                System.out.println("Deposit successful.");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 3:
                            System.out.print("Enter account ID: ");
                            accountId = scanner.nextInt();
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            try {
                                bankingSystem.withdraw(accountId, withdrawAmount);
                                System.out.println("Withdrawal successful.");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 4:
                            System.out.print("Enter source account ID: ");
                            int fromAccountId = scanner.nextInt();
                            System.out.print("Enter destination account ID: ");
                            int toAccountId = scanner.nextInt();
                            System.out.print("Enter amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            try {
                                bankingSystem.transfer(fromAccountId, toAccountId, transferAmount);
                                System.out.println("Transfer successful.");
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 5:
                            System.out.print("Enter account ID: ");
                            accountId = scanner.nextInt();
                            try {
                                double balance = bankingSystem.checkBalance(accountId);
                                System.out.println("Current balance: " + balance);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 6:
                            System.out.print("Enter account ID: ");
                            accountId = scanner.nextInt();
                            List<Transaction> transactions = bankingSystem.getTransactionHistory(accountId);
                            System.out.println("Transaction history for account ID " + accountId + ":");
                            for (Transaction transaction : transactions) {
                                System.out.println(transaction.getTimestamp() + " - " + transaction.getTransactionType() + ": " + transaction.getAmount());
                            }
                            break;
                        case 7:
                            currentUser = null;
                            System.out.println("Logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                    
                    if (currentUser == null) {
                        break;
                    }
                }
            }
        }
    }
}