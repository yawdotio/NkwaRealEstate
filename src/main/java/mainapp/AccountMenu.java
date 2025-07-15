package mainapp;

import accounts.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Menu handler for Bank Account Management functionality.
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
                    updateAccount();
                    break;
                case 5:
                    debitAccount();
                    break;
                case 6:
                    creditAccount();
                    break;
                case 7:
                    viewAccountsBelowThreshold();
                    break;
                case 8:
                    deleteAccount();
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
        System.out.println("\n--- Bank Account Management ---");
        System.out.println("1. Add Account");
        System.out.println("2. View All Accounts");
        System.out.println("3. View Account Details");
        System.out.println("4. Update Account");
        System.out.println("5. Debit Account");
        System.out.println("6. Credit Account");
        System.out.println("7. View Accounts Below Threshold");
        System.out.println("8. Delete Account");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addAccount() {
        System.out.println("\n--- Add New Account ---");
        
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter account name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter account type (Checking/Savings/Credit): ");
        String type = scanner.nextLine();
        
        System.out.print("Enter initial balance: ");
        BigDecimal balance = getBigDecimalInput();
        
        System.out.print("Enter bank name: ");
        String bankName = scanner.nextLine();
        
        Account account = new Account(id, name, type, balance, bankName);
        accountManager.addAccount(account);
        
        System.out.println("Account added successfully!");
    }
    
    private void viewAllAccounts() {
        System.out.println("\n--- All Accounts ---");
        Collection<Account> accounts = accountManager.getAllAccounts();
        
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
            System.out.println("ID: " + account.getAccountId());
            System.out.println("Name: " + account.getAccountName());
            System.out.println("Type: " + account.getAccountType());
            System.out.println("Balance: $" + account.getBalance());
            System.out.println("Bank: " + account.getBankName());
        }
    }
    
    private void updateAccount() {
        System.out.print("Enter account ID to update: ");
        String id = scanner.nextLine();
        
        Account account = accountManager.getAccount(id);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.println("Current account: " + account);
        System.out.println("Enter new values (press Enter to keep current value):");
        
        System.out.print("Name [" + account.getAccountName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            account.setAccountName(name);
        }
        
        System.out.print("Type [" + account.getAccountType() + "]: ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) {
            account.setAccountType(type);
        }
        
        System.out.print("Bank [" + account.getBankName() + "]: ");
        String bank = scanner.nextLine();
        if (!bank.isEmpty()) {
            account.setBankName(bank);
        }
        
        accountManager.updateAccount(account);
        System.out.println("Account updated successfully!");
    }
    
    private void debitAccount() {
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        Account account = accountManager.getAccount(id);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.println("Current balance: $" + account.getBalance());
        System.out.print("Enter amount to debit: ");
        BigDecimal amount = getBigDecimalInput();
        
        account.debit(amount);
        accountManager.updateAccount(account);
        
        System.out.println("Account debited successfully!");
        System.out.println("New balance: $" + account.getBalance());
    }
    
    private void creditAccount() {
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine();
        
        Account account = accountManager.getAccount(id);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.println("Current balance: $" + account.getBalance());
        System.out.print("Enter amount to credit: ");
        BigDecimal amount = getBigDecimalInput();
        
        account.credit(amount);
        accountManager.updateAccount(account);
        
        System.out.println("Account credited successfully!");
        System.out.println("New balance: $" + account.getBalance());
    }
    
    private void viewAccountsBelowThreshold() {
        System.out.print("Enter threshold amount: ");
        BigDecimal threshold = getBigDecimalInput();
        
        List<Account> accounts = accountManager.getAccountsBelowThreshold(threshold);
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found below threshold: $" + threshold);
        } else {
            System.out.println("\n--- Accounts Below $" + threshold + " ---");
            for (Account account : accounts) {
                System.out.println(account);
            }
        }
    }
    
    private void deleteAccount() {
        System.out.print("Enter account ID to delete: ");
        String id = scanner.nextLine();
        
        System.out.print("Are you sure? This action cannot be undone (y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            if (accountManager.removeAccount(id)) {
                System.out.println("Account deleted successfully!");
            } else {
                System.out.println("Account not found.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private BigDecimal getBigDecimalInput() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Please enter a valid number: ");
            }
        }
    }
}
