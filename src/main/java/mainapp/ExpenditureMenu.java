package mainapp;

import expenditures.Expenditure;
import expenditures.ExpenditureManager;
import expenditures.ExpenditureService;
import accounts.Account;
import accounts.AccountManager;
import categories.CategoryManager;
import receipts.ReceiptManager;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Menu handler for Expenditure Management functionality.
 * Updated to use the new integrated system with ExpenditureService.
 */
public class ExpenditureMenu {
    private ExpenditureManager expenditureManager;
    private AccountManager accountManager;
    private CategoryManager categoryManager;
    private ExpenditureService expenditureService;
    private ReceiptManager receiptManager;
    private Scanner scanner;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    
    public ExpenditureMenu(Scanner scanner) {
        this.scanner = scanner;
        this.expenditureManager = new ExpenditureManager();
        this.accountManager = new AccountManager();
        this.categoryManager = new CategoryManager();
        this.receiptManager = new ReceiptManager();
        this.expenditureService = new ExpenditureService(expenditureManager, accountManager, receiptManager);
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayExpenditureMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addExpenditure();
                    break;
                case 2:
                    viewAllExpenditures();
                    break;
                case 3:
                    viewExpendituresByAccount();
                    break;
                case 4:
                    viewExpendituresByCategory();
                    break;
                case 5:
                    viewExpendituresByPhase();
                    break;
                case 6:
                    viewAccountSummary();
                    break;
                case 7:
                    deleteExpenditure();
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
    
    private void displayExpenditureMenu() {
        System.out.println("\n--- Expenditure Management ---");
        System.out.println("1. Add Expenditure");
        System.out.println("2. View All Expenditures");
        System.out.println("3. View Expenditures by Account");
        System.out.println("4. View Expenditures by Category");
        System.out.println("5. View Expenditures by Phase");
        System.out.println("6. View Account Summary");
        System.out.println("7. Delete Expenditure");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addExpenditure() {
        System.out.println("\n--- Add New Expenditure ---");
        
        // Show available accounts
        System.out.println("Available Accounts:");
        for (Account account : accountManager.getAllAccounts()) {
            System.out.println("  " + account.getAccountId() + " - " + account.getBankName() + 
                             " (Balance: GHS " + account.getBalance() + ")");
        }
        
        System.out.print("\nEnter expenditure code: ");
        String code = scanner.nextLine();
        
        System.out.print("Enter amount: ");
        double amount = getDoubleInput();
        
        System.out.print("Enter date (dd-MM-yyyy): ");
        Date date = getDateInput();
        
        System.out.print("Enter phase (construction/marketing/sales): ");
        String phase = scanner.nextLine();
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        System.out.print("Enter receipt path (optional): ");
        String receiptPath = scanner.nextLine();
        if (receiptPath.trim().isEmpty()) {
            receiptPath = null;
        }
        
        // Use the service to create expenditure with proper account integration
        boolean success = expenditureService.createExpenditure(code, amount, date, phase, category, accountId, receiptPath);
        
        if (success) {
            // Add category if it doesn't exist
            categoryManager.addCategory(category);
            System.out.println("Expenditure added successfully!");
        } else {
            System.out.println("Failed to add expenditure. Please check the details.");
        }
    }
    
    private void viewAllExpenditures() {
        System.out.println("\n--- All Expenditures ---");
        List<Expenditure> expenditures = expenditureManager.getAll();
        
        if (expenditures.isEmpty()) {
            System.out.println("No expenditures found.");
        } else {
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
            }
        }
    }
    
    private void viewExpendituresByAccount() {
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        List<Expenditure> expenditures = expenditureManager.getExpendituresByAccount(accountId);
        
        if (expenditures.isEmpty()) {
            System.out.println("No expenditures found for account: " + accountId);
        } else {
            System.out.println("\n--- Expenditures for Account: " + accountId + " ---");
            double total = 0;
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
                total += exp.getAmount();
            }
            System.out.println("Total spent from this account: GHS " + total);
        }
    }
    
    private void viewExpendituresByCategory() {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        
        List<Expenditure> expenditures = expenditureManager.getExpendituresByCategory(category);
        
        if (expenditures.isEmpty()) {
            System.out.println("No expenditures found for category: " + category);
        } else {
            System.out.println("\n--- Expenditures for Category: " + category + " ---");
            double total = 0;
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
                total += exp.getAmount();
            }
            System.out.println("Total spent on this category: GHS " + total);
        }
    }
    
    private void viewExpendituresByPhase() {
        System.out.print("Enter phase: ");
        String phase = scanner.nextLine();
        
        List<Expenditure> expenditures = expenditureManager.searchByPhase(phase);
        
        if (expenditures.isEmpty()) {
            System.out.println("No expenditures found for phase: " + phase);
        } else {
            System.out.println("\n--- Expenditures for Phase: " + phase + " ---");
            double total = 0;
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
                total += exp.getAmount();
            }
            System.out.println("Total spent on this phase: GHS " + total);
        }
    }
    
    private void viewAccountSummary() {
        expenditureService.printAccountSummary();
    }
    
    private void deleteExpenditure() {
        System.out.println("\n--- Delete Expenditure ---");
        System.out.print("Enter expenditure code: ");
        String code = scanner.nextLine();
        
        if (expenditureManager.removeExpenditure(code)) {
            System.out.println("Expenditure deleted successfully!");
        } else {
            System.out.println("Expenditure not found.");
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
    
    private Date getDateInput() {
        while (true) {
            try {
                return DATE_FORMAT.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please enter date (dd-MM-yyyy): ");
            }
        }
    }
}
