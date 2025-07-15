package mainapp;

import tracker.*;
import accounts.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Menu handler for Bank Tracker functionality.
 */
public class BankTrackerMenu {
    private BankTracker bankTracker;
    private AccountManager accountManager;
    private Scanner scanner;
    
    public BankTrackerMenu(Scanner scanner) {
        this.scanner = scanner;
        this.bankTracker = new BankTracker();
        this.accountManager = new AccountManager();
        loadAccountsIntoTracker();
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayTrackerMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    viewLowestBalanceAccount();
                    break;
                case 2:
                    viewLowestBalanceAccounts();
                    break;
                case 3:
                    setAlertThreshold();
                    break;
                case 4:
                    viewAccountsBelowThreshold();
                    break;
                case 5:
                    viewAlerts();
                    break;
                case 6:
                    viewUnreadAlerts();
                    break;
                case 7:
                    markAlertAsRead();
                    break;
                case 8:
                    clearReadAlerts();
                    break;
                case 9:
                    viewBalanceStatistics();
                    break;
                case 10:
                    refreshTracker();
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
    
    private void displayTrackerMenu() {
        System.out.println("\n--- Bank Tracker (Min Heap) ---");
        System.out.println("1. View Lowest Balance Account");
        System.out.println("2. View N Lowest Balance Accounts");
        System.out.println("3. Set Alert Threshold");
        System.out.println("4. View Accounts Below Threshold");
        System.out.println("5. View All Alerts");
        System.out.println("6. View Unread Alerts");
        System.out.println("7. Mark Alert as Read");
        System.out.println("8. Clear Read Alerts");
        System.out.println("9. View Balance Statistics");
        System.out.println("10. Refresh Tracker");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void viewLowestBalanceAccount() {
        Account account = bankTracker.getLowestBalanceAccount();
        
        if (account == null) {
            System.out.println("No accounts being tracked.");
        } else {
            System.out.println("\n--- Lowest Balance Account ---");
            System.out.println(account);
            System.out.println("Balance: $" + account.getBalance());
        }
    }
    
    private void viewLowestBalanceAccounts() {
        System.out.print("Enter number of accounts to display: ");
        int n = getChoice();
        
        if (n <= 0) {
            System.out.println("Invalid number.");
            return;
        }
        
        List<Account> accounts = bankTracker.getLowestBalanceAccounts(n);
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts being tracked.");
        } else {
            System.out.println("\n--- " + n + " Lowest Balance Accounts ---");
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                System.out.println((i + 1) + ". " + account.getAccountName() + 
                                 " (" + account.getAccountId() + ") - $" + account.getBalance());
            }
        }
    }
    
    private void setAlertThreshold() {
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        System.out.print("Enter alert threshold amount: ");
        BigDecimal threshold = getBigDecimalInput();
        
        bankTracker.setAlertThreshold(accountId, threshold);
        System.out.println("Alert threshold set for account " + accountId + ": $" + threshold);
    }
    
    private void viewAccountsBelowThreshold() {
        List<Account> accounts = bankTracker.getAccountsBelowThreshold();
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts are below their alert thresholds.");
        } else {
            System.out.println("\n--- Accounts Below Alert Thresholds ---");
            for (Account account : accounts) {
                BigDecimal threshold = bankTracker.getAlertThreshold(account.getAccountId());
                System.out.println(account.getAccountName() + " (" + account.getAccountId() + ")");
                System.out.println("  Current Balance: $" + account.getBalance());
                System.out.println("  Alert Threshold: $" + threshold);
                System.out.println();
            }
        }
    }
    
    private void viewAlerts() {
        List<BalanceAlert> alerts = bankTracker.getAlerts();
        
        if (alerts.isEmpty()) {
            System.out.println("No alerts found.");
        } else {
            System.out.println("\n--- All Alerts ---");
            for (BalanceAlert alert : alerts) {
                System.out.println("[" + (alert.isRead() ? "READ" : "UNREAD") + "] " + alert.getMessage());
                System.out.println("  Alert ID: " + alert.getAlertId());
                System.out.println("  Date: " + alert.getAlertDate());
                System.out.println();
            }
        }
    }
    
    private void viewUnreadAlerts() {
        List<BalanceAlert> unreadAlerts = bankTracker.getUnreadAlerts();
        
        if (unreadAlerts.isEmpty()) {
            System.out.println("No unread alerts.");
        } else {
            System.out.println("\n--- Unread Alerts ---");
            for (BalanceAlert alert : unreadAlerts) {
                System.out.println(alert.getMessage());
                System.out.println("  Alert ID: " + alert.getAlertId());
                System.out.println("  Date: " + alert.getAlertDate());
                System.out.println();
            }
        }
    }
    
    private void markAlertAsRead() {
        System.out.print("Enter alert ID to mark as read: ");
        String alertId = scanner.nextLine();
        
        bankTracker.markAlertAsRead(alertId);
        System.out.println("Alert marked as read.");
    }
    
    private void clearReadAlerts() {
        bankTracker.clearReadAlerts();
        System.out.println("All read alerts have been cleared.");
    }
    
    private void viewBalanceStatistics() {
        BalanceStatistics stats = bankTracker.getBalanceStatistics();
        
        System.out.println("\n--- Balance Statistics ---");
        System.out.println("Number of Accounts: " + stats.getAccountCount());
        System.out.println("Minimum Balance: $" + stats.getMinBalance());
        System.out.println("Maximum Balance: $" + stats.getMaxBalance());
        System.out.println("Average Balance: $" + stats.getAverageBalance());
        System.out.println("Total Balance: $" + bankTracker.getTotalBalance());
    }
    
    private void refreshTracker() {
        // Reload accounts from AccountManager and update tracker
        loadAccountsIntoTracker();
        System.out.println("Tracker refreshed with latest account data.");
    }
    
    private void loadAccountsIntoTracker() {
        // Clear current tracker and reload with fresh data
        Collection<Account> accounts = accountManager.getAllAccounts();
        for (Account account : accounts) {
            bankTracker.updateAccount(account);
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
