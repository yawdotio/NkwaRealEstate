package mainapp;

import expenditures.*;
import accounts.AccountManager;
import categories.CategoryManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Menu handler for Expenditure Management functionality.
 */
public class ExpenditureMenu {
    private ExpenditureManager expenditureManager;
    private AccountManager accountManager;
    private CategoryManager categoryManager;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ExpenditureMenu(Scanner scanner) {
        this.scanner = scanner;
        this.expenditureManager = new ExpenditureManager();
        this.accountManager = new AccountManager();
        this.categoryManager = new CategoryManager();
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
                    viewExpendituresByDateRange();
                    break;
                case 6:
                    updateExpenditure();
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
        System.out.println("5. View Expenditures by Date Range");
        System.out.println("6. Update Expenditure");
        System.out.println("7. Delete Expenditure");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addExpenditure() {
        System.out.println("\n--- Add New Expenditure ---");
        
        System.out.print("Enter expenditure ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter amount: ");
        BigDecimal amount = getBigDecimalInput();
        
        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = getDateInput();
        
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        System.out.print("Enter category ID: ");
        String categoryId = scanner.nextLine();
        
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        
        System.out.print("Enter project ID: ");
        String projectId = scanner.nextLine();
        
        Expenditure expenditure = new Expenditure(id, description, amount, date, accountId, categoryId, vendor, projectId);
        expenditureManager.addExpenditure(expenditure);
        
        System.out.println("Expenditure added successfully!");
    }
    
    private void viewAllExpenditures() {
        System.out.println("\n--- All Expenditures ---");
        Collection<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        
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
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
            }
        }
    }
    
    private void viewExpendituresByCategory() {
        System.out.print("Enter category ID: ");
        String categoryId = scanner.nextLine();
        
        List<Expenditure> expenditures = expenditureManager.getExpendituresByCategory(categoryId);
        
        if (expenditures.isEmpty()) {
            System.out.println("No expenditures found for category: " + categoryId);
        } else {
            System.out.println("\n--- Expenditures for Category: " + categoryId + " ---");
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
            }
        }
    }
    
    private void viewExpendituresByDateRange() {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        List<Expenditure> expenditures = expenditureManager.getExpendituresByDateRange(startDate, endDate);
        
        if (expenditures.isEmpty()) {
            System.out.println("No expenditures found in the specified date range.");
        } else {
            System.out.println("\n--- Expenditures from " + startDate + " to " + endDate + " ---");
            for (Expenditure exp : expenditures) {
                System.out.println(exp);
            }
        }
    }
    
    private void updateExpenditure() {
        System.out.print("Enter expenditure ID to update: ");
        String id = scanner.nextLine();
        
        Expenditure expenditure = expenditureManager.getExpenditure(id);
        if (expenditure == null) {
            System.out.println("Expenditure not found.");
            return;
        }
        
        System.out.println("Current expenditure: " + expenditure);
        System.out.println("Enter new values (press Enter to keep current value):");
        
        System.out.print("Description [" + expenditure.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            expenditure.setDescription(description);
        }
        
        System.out.print("Amount [" + expenditure.getAmount() + "]: ");
        String amountStr = scanner.nextLine();
        if (!amountStr.isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                expenditure.setAmount(amount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Keeping current value.");
            }
        }
        
        expenditureManager.updateExpenditure(expenditure);
        System.out.println("Expenditure updated successfully!");
    }
    
    private void deleteExpenditure() {
        System.out.print("Enter expenditure ID to delete: ");
        String id = scanner.nextLine();
        
        if (expenditureManager.removeExpenditure(id)) {
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
    
    private BigDecimal getBigDecimalInput() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Please enter a valid number: ");
            }
        }
    }
    
    private LocalDate getDateInput() {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }
}
