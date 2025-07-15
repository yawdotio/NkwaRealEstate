package mainapp;

import expenditures.*;
import searchsort.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Menu handler for Search and Sort functionality.
 */
public class SearchSortMenu {
    private ExpenditureManager expenditureManager;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
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
                    searchByDescription();
                    break;
                case 2:
                    searchByVendor();
                    break;
                case 3:
                    filterByAmountRange();
                    break;
                case 4:
                    filterByDateRange();
                    break;
                case 5:
                    filterByCategory();
                    break;
                case 6:
                    filterByAccount();
                    break;
                case 7:
                    multiFieldSearch();
                    break;
                case 8:
                    sortExpenditures();
                    break;
                case 9:
                    getTopExpenditures();
                    break;
                case 10:
                    groupExpenditures();
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
    
    private void displaySearchSortMenu() {
        System.out.println("\n--- Search & Sort ---");
        System.out.println("1. Search by Description");
        System.out.println("2. Search by Vendor");
        System.out.println("3. Filter by Amount Range");
        System.out.println("4. Filter by Date Range");
        System.out.println("5. Filter by Category");
        System.out.println("6. Filter by Account");
        System.out.println("7. Multi-field Search");
        System.out.println("8. Sort Expenditures");
        System.out.println("9. Get Top Expenditures");
        System.out.println("10. Group Expenditures");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void searchByDescription() {
        System.out.print("Enter description to search for: ");
        String searchTerm = scanner.nextLine();
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.searchByDescription(expenditures, searchTerm);
        
        displayResults("Search Results for Description: " + searchTerm, results);
    }
    
    private void searchByVendor() {
        System.out.print("Enter vendor name to search for: ");
        String vendor = scanner.nextLine();
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.searchByVendor(expenditures, vendor);
        
        displayResults("Search Results for Vendor: " + vendor, results);
    }
    
    private void filterByAmountRange() {
        System.out.print("Enter minimum amount: ");
        BigDecimal minAmount = getBigDecimalInput();
        
        System.out.print("Enter maximum amount: ");
        BigDecimal maxAmount = getBigDecimalInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.filterByAmountRange(expenditures, minAmount, maxAmount);
        
        displayResults("Expenditures between $" + minAmount + " and $" + maxAmount, results);
    }
    
    private void filterByDateRange() {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.filterByDateRange(expenditures, startDate, endDate);
        
        displayResults("Expenditures from " + startDate + " to " + endDate, results);
    }
    
    private void filterByCategory() {
        System.out.print("Enter category ID: ");
        String categoryId = scanner.nextLine();
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.filterByCategory(expenditures, categoryId);
        
        displayResults("Expenditures for Category: " + categoryId, results);
    }
    
    private void filterByAccount() {
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.filterByAccount(expenditures, accountId);
        
        displayResults("Expenditures for Account: " + accountId, results);
    }
    
    private void multiFieldSearch() {
        System.out.println("\n--- Multi-field Search ---");
        System.out.println("Enter criteria (press Enter to skip any field):");
        
        SearchCriteria criteria = new SearchCriteria();
        
        System.out.print("Description contains: ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            criteria.setDescription(description);
        }
        
        System.out.print("Vendor contains: ");
        String vendor = scanner.nextLine();
        if (!vendor.isEmpty()) {
            criteria.setVendor(vendor);
        }
        
        System.out.print("Minimum amount: ");
        String minAmountStr = scanner.nextLine();
        if (!minAmountStr.isEmpty()) {
            try {
                criteria.setMinAmount(new BigDecimal(minAmountStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid minimum amount format. Skipping.");
            }
        }
        
        System.out.print("Maximum amount: ");
        String maxAmountStr = scanner.nextLine();
        if (!maxAmountStr.isEmpty()) {
            try {
                criteria.setMaxAmount(new BigDecimal(maxAmountStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid maximum amount format. Skipping.");
            }
        }
        
        System.out.print("Start date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        if (!startDateStr.isEmpty()) {
            try {
                criteria.setStartDate(LocalDate.parse(startDateStr, DATE_FORMAT));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid start date format. Skipping.");
            }
        }
        
        System.out.print("End date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        if (!endDateStr.isEmpty()) {
            try {
                criteria.setEndDate(LocalDate.parse(endDateStr, DATE_FORMAT));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid end date format. Skipping.");
            }
        }
        
        System.out.print("Category ID: ");
        String categoryId = scanner.nextLine();
        if (!categoryId.isEmpty()) {
            criteria.setCategoryId(categoryId);
        }
        
        System.out.print("Account ID: ");
        String accountId = scanner.nextLine();
        if (!accountId.isEmpty()) {
            criteria.setAccountId(accountId);
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> results = SearchSortUtils.multiFieldSearch(expenditures, criteria);
        
        displayResults("Multi-field Search Results", results);
    }
    
    private void sortExpenditures() {
        System.out.println("\n--- Sort Expenditures ---");
        System.out.println("1. Sort by Date (Newest First)");
        System.out.println("2. Sort by Date (Oldest First)");
        System.out.println("3. Sort by Amount (Highest First)");
        System.out.println("4. Sort by Amount (Lowest First)");
        System.out.println("5. Sort by Vendor");
        System.out.println("6. Sort by Description");
        System.out.print("Enter choice: ");
        
        int sortChoice = getChoice();
        Expenditure[] expenditures = getExpenditureArray();
        Expenditure[] sortedExpenditures;
        
        switch (sortChoice) {
            case 1:
                sortedExpenditures = SearchSortUtils.sortByDate(expenditures, true);
                break;
            case 2:
                sortedExpenditures = SearchSortUtils.sortByDate(expenditures, false);
                break;
            case 3:
                sortedExpenditures = SearchSortUtils.sortByAmount(expenditures, true);
                break;
            case 4:
                sortedExpenditures = SearchSortUtils.sortByAmount(expenditures, false);
                break;
            case 5:
                sortedExpenditures = SearchSortUtils.sortByVendor(expenditures);
                break;
            case 6:
                sortedExpenditures = SearchSortUtils.sortByDescription(expenditures);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        displayResults("Sorted Expenditures", Arrays.asList(sortedExpenditures));
    }
    
    private void getTopExpenditures() {
        System.out.print("Enter number of top expenditures to display: ");
        int n = getChoice();
        
        if (n <= 0) {
            System.out.println("Invalid number.");
            return;
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        List<Expenditure> topExpenditures = SearchSortUtils.getTopExpenditures(expenditures, n);
        
        displayResults("Top " + n + " Expenditures by Amount", topExpenditures);
    }
    
    private void groupExpenditures() {
        System.out.println("\n--- Group Expenditures ---");
        System.out.println("1. Group by Vendor");
        System.out.println("2. Group by Category");
        System.out.print("Enter choice: ");
        
        int groupChoice = getChoice();
        Expenditure[] expenditures = getExpenditureArray();
        
        switch (groupChoice) {
            case 1:
                Map<String, List<Expenditure>> vendorGroups = SearchSortUtils.groupByVendor(expenditures);
                displayGroupedResults("Expenditures Grouped by Vendor", vendorGroups);
                break;
            case 2:
                Map<String, List<Expenditure>> categoryGroups = SearchSortUtils.groupByCategory(expenditures);
                displayGroupedResults("Expenditures Grouped by Category", categoryGroups);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void displayResults(String title, List<Expenditure> results) {
        System.out.println("\n--- " + title + " ---");
        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            System.out.println("Found " + results.size() + " result(s):");
            for (Expenditure exp : results) {
                System.out.println(exp);
            }
        }
    }
    
    private void displayGroupedResults(String title, Map<String, List<Expenditure>> groups) {
        System.out.println("\n--- " + title + " ---");
        if (groups.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (Map.Entry<String, List<Expenditure>> entry : groups.entrySet()) {
                System.out.println("\n" + entry.getKey() + " (" + entry.getValue().size() + " expenditures):");
                for (Expenditure exp : entry.getValue()) {
                    System.out.println("  " + exp);
                }
            }
        }
    }
    
    private Expenditure[] getExpenditureArray() {
        Collection<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        return expenditures.toArray(new Expenditure[0]);
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
