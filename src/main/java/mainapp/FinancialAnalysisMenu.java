package mainapp;

import analysis.*;
import expenditures.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Menu handler for Financial Analysis functionality.
 */
public class FinancialAnalysisMenu {
    private ExpenditureManager expenditureManager;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public FinancialAnalysisMenu(Scanner scanner) {
        this.scanner = scanner;
        this.expenditureManager = new ExpenditureManager();
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayAnalysisMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    calculateBurnRate();
                    break;
                case 2:
                    analyzeMonthlySpending();
                    break;
                case 3:
                    analyzeByCategoryIds();
                    break;
                case 4:
                    analyzeByVendor();
                    break;
                case 5:
                    projectFutureSpending();
                    break;
                case 6:
                    calculateBudgetVariance();
                    break;
                case 7:
                    analyzeTrends();
                    break;
                case 8:
                    getTopSpendingCategories();
                    break;
                case 9:
                    calculateEfficiencyMetrics();
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
    
    private void displayAnalysisMenu() {
        System.out.println("\n--- Financial Analysis ---");
        System.out.println("1. Calculate Burn Rate");
        System.out.println("2. Analyze Monthly Spending");
        System.out.println("3. Analyze by Category");
        System.out.println("4. Analyze by Vendor");
        System.out.println("5. Project Future Spending");
        System.out.println("6. Calculate Budget Variance");
        System.out.println("7. Analyze Trends");
        System.out.println("8. Top Spending Categories");
        System.out.println("9. Calculate Efficiency Metrics");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void calculateBurnRate() {
        System.out.println("\n--- Calculate Burn Rate ---");
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        BigDecimal burnRate = FinancialAnalysis.calculateBurnRate(expenditures, startDate, endDate);
        
        System.out.println("\n--- Burn Rate Analysis ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Daily Burn Rate: $" + burnRate);
        System.out.println("Weekly Burn Rate: $" + burnRate.multiply(BigDecimal.valueOf(7)));
        System.out.println("Monthly Burn Rate: $" + burnRate.multiply(BigDecimal.valueOf(30)));
    }
    
    private void analyzeMonthlySpending() {
        System.out.print("Enter year to analyze: ");
        int year = getChoice();
        
        if (year < 2000 || year > 2100) {
            System.out.println("Invalid year.");
            return;
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        Map<String, BigDecimal> monthlySpending = FinancialAnalysis.calculateMonthlySpending(expenditures, year);
        
        System.out.println("\n--- Monthly Spending Analysis for " + year + " ---");
        BigDecimal yearTotal = BigDecimal.ZERO;
        
        for (Map.Entry<String, BigDecimal> entry : monthlySpending.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
            yearTotal = yearTotal.add(entry.getValue());
        }
        
        System.out.println("Total for " + year + ": $" + yearTotal);
        BigDecimal avgMonthly = yearTotal.divide(BigDecimal.valueOf(12), 2, java.math.RoundingMode.HALF_UP);
        System.out.println("Average Monthly: $" + avgMonthly);
    }
    
    private void analyzeByCategoryIds() {
        Expenditure[] expenditures = getExpenditureArray();
        Map<String, CategoryAnalysis> analysis = FinancialAnalysis.analyzeByCategoryIds(expenditures);
        
        System.out.println("\n--- Category Analysis ---");
        if (analysis.isEmpty()) {
            System.out.println("No expenditures found for analysis.");
        } else {
            for (Map.Entry<String, CategoryAnalysis> entry : analysis.entrySet()) {
                CategoryAnalysis catAnalysis = entry.getValue();
                System.out.println("\nCategory: " + entry.getKey());
                System.out.println("  Total Amount: $" + catAnalysis.getTotalAmount());
                System.out.println("  Transaction Count: " + catAnalysis.getTransactionCount());
                System.out.println("  Average Amount: $" + catAnalysis.getAverageAmount());
                System.out.println("  First Transaction: " + catAnalysis.getFirstTransaction());
                System.out.println("  Last Transaction: " + catAnalysis.getLastTransaction());
            }
        }
    }
    
    private void analyzeByVendor() {
        Expenditure[] expenditures = getExpenditureArray();
        Map<String, VendorAnalysis> analysis = FinancialAnalysis.analyzeByVendor(expenditures);
        
        System.out.println("\n--- Vendor Analysis ---");
        if (analysis.isEmpty()) {
            System.out.println("No expenditures found for analysis.");
        } else {
            for (Map.Entry<String, VendorAnalysis> entry : analysis.entrySet()) {
                VendorAnalysis vendorAnalysis = entry.getValue();
                System.out.println("\nVendor: " + entry.getKey());
                System.out.println("  Total Amount: $" + vendorAnalysis.getTotalAmount());
                System.out.println("  Transaction Count: " + vendorAnalysis.getTransactionCount());
                System.out.println("  Average Amount: $" + vendorAnalysis.getAverageAmount());
                System.out.println("  First Transaction: " + vendorAnalysis.getFirstTransaction());
                System.out.println("  Last Transaction: " + vendorAnalysis.getLastTransaction());
            }
        }
    }
    
    private void projectFutureSpending() {
        System.out.println("\n--- Project Future Spending ---");
        System.out.print("Enter historical start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter historical end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        System.out.print("Enter number of future days to project: ");
        int futureDays = getChoice();
        
        if (futureDays <= 0) {
            System.out.println("Invalid number of days.");
            return;
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        BigDecimal projection = FinancialAnalysis.projectFutureSpending(expenditures, startDate, endDate, futureDays);
        
        System.out.println("\n--- Future Spending Projection ---");
        System.out.println("Based on period: " + startDate + " to " + endDate);
        System.out.println("Projected spending for next " + futureDays + " days: $" + projection);
    }
    
    private void calculateBudgetVariance() {
        System.out.println("\n--- Budget Variance Analysis ---");
        System.out.print("Enter planned budget amount: ");
        BigDecimal plannedBudget = getBigDecimalInput();
        
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        BigDecimal variance = FinancialAnalysis.calculateBudgetVariance(expenditures, plannedBudget, startDate, endDate);
        
        System.out.println("\n--- Budget Variance Results ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Planned Budget: $" + plannedBudget);
        System.out.println("Actual Spending: $" + plannedBudget.add(variance));
        System.out.println("Variance: $" + variance);
        
        if (variance.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Status: OVER BUDGET by $" + variance);
        } else if (variance.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Status: UNDER BUDGET by $" + variance.abs());
        } else {
            System.out.println("Status: ON BUDGET");
        }
    }
    
    private void analyzeTrends() {
        System.out.println("\n--- Trend Analysis ---");
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        TrendAnalysis trendAnalysis = FinancialAnalysis.analyzeTrends(expenditures, startDate, endDate);
        
        System.out.println("\n--- Trend Analysis Results ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Overall Trend: " + trendAnalysis.getTrendDirection());
        
        List<BigDecimal> weeklySpending = trendAnalysis.getWeeklySpending();
        System.out.println("\nWeekly Spending Breakdown:");
        for (int i = 0; i < weeklySpending.size(); i++) {
            System.out.println("Week " + (i + 1) + ": $" + weeklySpending.get(i));
        }
    }
    
    private void getTopSpendingCategories() {
        System.out.print("Enter number of top categories to display: ");
        int topN = getChoice();
        
        if (topN <= 0) {
            System.out.println("Invalid number.");
            return;
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        List<CategorySummary> topCategories = FinancialAnalysis.getTopSpendingCategories(expenditures, topN);
        
        System.out.println("\n--- Top " + topN + " Spending Categories ---");
        if (topCategories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            for (int i = 0; i < topCategories.size(); i++) {
                CategorySummary summary = topCategories.get(i);
                System.out.println((i + 1) + ". " + summary.getCategoryId() + ": $" + summary.getTotalAmount());
            }
        }
    }
    
    private void calculateEfficiencyMetrics() {
        System.out.println("\n--- Efficiency Metrics ---");
        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        EfficiencyMetrics metrics = FinancialAnalysis.calculateEfficiencyMetrics(expenditures, startDate, endDate);
        
        System.out.println("\n--- Efficiency Metrics Results ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Total Spent: $" + metrics.getTotalSpent());
        System.out.println("Transaction Count: " + metrics.getTransactionCount());
        System.out.println("Unique Vendors: " + metrics.getUniqueVendorCount());
        System.out.println("Average Transaction Amount: $" + metrics.getAverageTransactionAmount());
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
