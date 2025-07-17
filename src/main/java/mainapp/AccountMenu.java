package mainapp;

import accounts.*;
import java.util.*;

/**
 * Menu handler for Bank Account Management functionality.
 * Updated to work with the new Account structure.
 */
public class AccountMenu {
    private AccountManager accountManager;
    private Scanner scanner;
    
    public AccountMenu(Scanner scanner) {
        this.scanner = scanner;
        this.accountManager = new AccountManager();
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayAccountMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    viewAllAccounts();
                    break;
                case 3:
                    viewAccountDetails();
                    break;
                case 4:
                    depositToAccount();
                    break;
                case 5:
                    withdrawFromAccount();
                    break;
                case 6:
                    viewAccountExpenditures();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void displayAccountMenu() {
        System.out.println("\n--- Account Management ---");
        System.out.println("1. Add Account");
        System.out.println("2. View All Accounts");
        System.out.println("3. View Account Details");
        System.out.println("4. Deposit to Account");
        System.out.println("5. Withdraw from Account");
        System.out.println("6. View Account Expenditures");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addAccount() {
        System.out.println("\n--- Add New Account ---");
        
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();
        
        System.out.print("Enter initial balance: ");
        double balance = getDoubleInput();
        
        Account account = new Account(id, bankName, balance);
        accountManager.addAccount(account);
        
        System.out.println("Account added successfully!");
    }
    
    private void viewAllAccounts() {
        System.out.println("\n--- All Accounts ---");
        List<Account> accounts = accountManager.getAllAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts) {
                System.out.println(account);
            }
        }
    }
    
    private void viewAccountDetails() {
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        Account account = accountManager.getAccount(id);
        if (account == null) {
            System.out.println("Account not found.");
        } else {
            System.out.println("\n--- Account Details ---");
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Bank Name: " + account.getBankName());
            System.out.println("Balance: GHS " + account.getBalance());
            System.out.println("Number of Expenditures: " + account.getExpenditureCodes().size());
            
            if (!account.getExpenditureCodes().isEmpty()) {
                System.out.println("\nExpenditure Codes:");
                for (String code : account.getExpenditureCodes()) {
                    System.out.println("  - " + code);
                }
            }
        }
    }
    
    private void depositToAccount() {
        System.out.println("\n--- Deposit to Account ---");
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter deposit amount: ");
        double amount = getDoubleInput();
        
        if (accountManager.depositToAccount(id, amount)) {
            System.out.println("Deposit successful!");
            Account account = accountManager.getAccount(id);
            System.out.println("New balance: GHS " + account.getBalance());
        } else {
            System.out.println("Deposit failed. Account not found.");
        }
    }
    
    private void withdrawFromAccount() {
        System.out.println("\n--- Withdraw from Account ---");
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        Account account = accountManager.getAccount(id);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.println("Current balance: GHS " + account.getBalance());
        System.out.print("Enter withdrawal amount: ");
        double amount = getDoubleInput();
        
        if (accountManager.withdrawFromAccount(id, amount)) {
            System.out.println("Withdrawal successful!");
            System.out.println("New balance: GHS " + account.getBalance());
        } else {
            System.out.println("Withdrawal failed. Insufficient funds.");
        }
    }
    
    private void viewAccountExpenditures() {
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        Account account = accountManager.getAccount(id);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.println("\n--- Expenditures for Account: " + id + " ---");
        List<String> expenditureCodes = account.getExpenditureCodes();
        
        if (expenditureCodes.isEmpty()) {
            System.out.println("No expenditures found for this account.");
        } else {
            System.out.println("Expenditure codes:");
            for (String code : expenditureCodes) {
                System.out.println("  - " + code);
            }
            System.out.println("Total expenditures: " + expenditureCodes.size());
        }
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number format. Please enter a valid amount: ");
            }
        }
    }
}
