package mainapp;

import expenditures.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Menu handler for Search and Sort functionality.
 * Uses only the NkwaExpenditureSystem search and sort methods.
 */
public class SearchSortMenu {
    private ExpenditureManager expenditureManager;
    private Scanner scanner;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public SearchSortMenu(Scanner scanner) {
        this.scanner = scanner;
        this.expenditureManager = new ExpenditureManager();
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displaySearchSortMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    filterByAmountRange();
                    break;
                case 2:
                    filterByDateRange();
                    break;
                case 3:
                    filterByCategory();
                    break;
                case 4:
                    filterByAccount();
                    break;
                case 5:
                    sortByCategory();
                    break;
                case 6:
                    sortByDate();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void displaySearchSortMenu() {
        System.out.println("\\n--- Search & Sort (NkwaExpenditureSystem) ---");
        System.out.println("1. Filter by Amount Range");
        System.out.println("2. Filter by Date Range");
        System.out.println("3. Filter by Category");
        System.out.println("4. Filter by Account");
        System.out.println("5. Sort by Category");
        System.out.println("6. Sort by Date");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void filterByAmountRange() {
        System.out.print("Enter minimum amount: ");
        double minAmount = getDoubleInput();
        
        System.out.print("Enter maximum amount: ");
        double maxAmount = getDoubleInput();
        
        // Use NkwaExpenditureSystem style cost range search
        List<Expenditure> results = searchByCostRange(minAmount, maxAmount);
        
        displayResults("Amount Range Filter Results (GHS " + minAmount + " - GHS " + maxAmount + ")", results);
    }
    
    private void filterByDateRange() {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        Date startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        Date endDate = getDateInput();
        
        List<Expenditure> results = expenditureManager.searchByDateRange(startDate, endDate);
        
        displayResults("Date Range Filter Results (" + DATE_FORMAT.format(startDate) + " to " + DATE_FORMAT.format(endDate) + ")", results);
    }
    
    private void filterByCategory() {
        System.out.print("Enter category: ");
        String categoryId = scanner.nextLine();
        
        List<Expenditure> results = expenditureManager.searchByCategory(categoryId);
        
        displayResults("Category Filter Results for: " + categoryId, results);
    }
    
    private void filterByAccount() {
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        List<Expenditure> results = expenditureManager.searchByAccountId(accountId);
        
        displayResults("Account Filter Results for: " + accountId, results);
    }
    
    private void sortByCategory() {
        List<Expenditure> sortedResults = sortByCategoryLocal();
        
        displayResults("Expenditures Sorted by Category", sortedResults);
    }
    
    private void sortByDate() {
        List<Expenditure> sortedResults = sortByDateLocal();
        
        displayResults("Expenditures Sorted by Date", sortedResults);
    }
    
    // NkwaExpenditureSystem style methods
    private List<Expenditure> searchByCostRange(double min, double max) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditureManager.getAll()) {
            if (exp.getAmount() >= min && exp.getAmount() <= max) {
                result.add(exp);
            }
        }
        return result;
    }
    
    private List<Expenditure> sortByCategoryLocal() {
        List<Expenditure> list = new ArrayList<>(expenditureManager.getAll());
        // Insertion Sort by category (NkwaExpenditureSystem style)
        for (int i = 1; i < list.size(); i++) {
            Expenditure key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getCategory().compareToIgnoreCase(key.getCategory()) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        return list;
    }
    
    private List<Expenditure> sortByDateLocal() {
        List<Expenditure> list = new ArrayList<>(expenditureManager.getAll());
        // Insertion Sort by date (NkwaExpenditureSystem style)
        for (int i = 1; i < list.size(); i++) {
            Expenditure key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getDate().after(key.getDate())) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        return list;
    }
    
    private void displayResults(String title, List<Expenditure> results) {
        System.out.println("\\n--- " + title + " ---");
        
        if (results.isEmpty()) {
            System.out.println("No expenditures found matching the criteria.");
        } else {
            System.out.println("Found " + results.size() + " expenditure(s):");
            System.out.println();
            
            for (Expenditure exp : results) {
                System.out.println(exp);
            }
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
                System.out.print("Invalid amount. Please enter a valid number: ");
            }
        }
    }
    
    private Date getDateInput() {
        while (true) {
            try {
                return DATE_FORMAT.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }
}
