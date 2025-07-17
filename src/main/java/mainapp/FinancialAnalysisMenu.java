package mainapp;

import analysis.*;
import expenditures.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Menu handler for Financial Analysis functionality.
 */
public class FinancialAnalysisMenu {
    private ExpenditureManager expenditureManager;
    private Scanner scanner;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
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
        Date startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        Date endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        double burnRate = FinancialAnalysis.calculateBurnRate(expenditures, startDate, endDate);
        
        System.out.println("\n--- Burn Rate Analysis ---");
        System.out.println("Period: " + DATE_FORMAT.format(startDate) + " to " + DATE_FORMAT.format(endDate));
        System.out.println("Daily Burn Rate: $" + String.format("%.2f", burnRate));
        System.out.println("Weekly Burn Rate: $" + String.format("%.2f", burnRate * 7));
        System.out.println("Monthly Burn Rate: $" + String.format("%.2f", burnRate * 30));
    }
    
    private void analyzeMonthlySpending() {
        System.out.print("Enter year to analyze: ");
        int year = getChoice();
        
        if (year < 2000 || year > 2100) {
            System.out.println("Invalid year.");
            return;
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        Map<String, Double> monthlySpending = FinancialAnalysis.calculateMonthlySpending(expenditures, year);
        
        System.out.println("\n--- Monthly Spending Analysis for " + year + " ---");
        double yearTotal = 0.0;
        
        for (Map.Entry<String, Double> entry : monthlySpending.entrySet()) {
            System.out.println(entry.getKey() + ": $" + String.format("%.2f", entry.getValue()));
            yearTotal += entry.getValue();
        }
        
        System.out.println("Total for " + year + ": $" + String.format("%.2f", yearTotal));
        double avgMonthly = yearTotal / 12.0;
        System.out.println("Average Monthly: $" + String.format("%.2f", avgMonthly));
    }
    
    private void analyzeByCategoryIds() {
        Expenditure[] expenditures = getExpenditureArray();
        Map<String, CategoryAnalysis> analysis = FinancialAnalysis.analyzeByCategory(expenditures);
        
        System.out.println("\n--- Category Analysis ---");
        if (analysis.isEmpty()) {
            System.out.println("No expenditures found for analysis.");
        } else {
            for (Map.Entry<String, CategoryAnalysis> entry : analysis.entrySet()) {
                CategoryAnalysis catAnalysis = entry.getValue();
                System.out.println("\nCategory: " + entry.getKey());
                System.out.println("  Total Amount: $" + String.format("%.2f", catAnalysis.getTotalAmount()));
                System.out.println("  Transaction Count: " + catAnalysis.getTransactionCount());
                System.out.println("  Average Amount: $" + String.format("%.2f", catAnalysis.getAverageAmount()));
                System.out.println("  First Transaction: " + (catAnalysis.getFirstTransaction() != null ? 
                    DATE_FORMAT.format(catAnalysis.getFirstTransaction()) : "None"));
                System.out.println("  Last Transaction: " + (catAnalysis.getLastTransaction() != null ? 
                    DATE_FORMAT.format(catAnalysis.getLastTransaction()) : "None"));
            }
        }
    }
    
    private void analyzeByVendor() {
        Expenditure[] expenditures = getExpenditureArray();
        // For now, we'll analyze by phase since the new structure doesn't have vendors
        // but phases which are similar concept
        System.out.println("\n--- Phase Analysis ---");
        System.out.println("(Note: Using 'phase' field as it's the closest equivalent to vendor analysis)");
        
        Map<String, VendorAnalysis> analysis = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String phase = exp.getPhase();
            VendorAnalysis vendorAnalysis = analysis.get(phase);
            
            if (vendorAnalysis == null) {
                vendorAnalysis = new VendorAnalysis(phase);
                analysis.put(phase, vendorAnalysis);
            }
            
            vendorAnalysis.addExpenditure(exp);
        }
        
        if (analysis.isEmpty()) {
            System.out.println("No expenditures found for analysis.");
        } else {
            for (Map.Entry<String, VendorAnalysis> entry : analysis.entrySet()) {
                VendorAnalysis vendorAnalysis = entry.getValue();
                System.out.println("\nPhase: " + entry.getKey());
                System.out.println("  Total Amount: $" + String.format("%.2f", vendorAnalysis.getTotalAmount()));
                System.out.println("  Transaction Count: " + vendorAnalysis.getTransactionCount());
                System.out.println("  Average Amount: $" + String.format("%.2f", vendorAnalysis.getAverageAmount()));
                System.out.println("  First Transaction: " + (vendorAnalysis.getFirstTransaction() != null ? 
                    DATE_FORMAT.format(vendorAnalysis.getFirstTransaction()) : "None"));
                System.out.println("  Last Transaction: " + (vendorAnalysis.getLastTransaction() != null ? 
                    DATE_FORMAT.format(vendorAnalysis.getLastTransaction()) : "None"));
            }
        }
    }
    
    private void projectFutureSpending() {
        System.out.println("\n--- Project Future Spending ---");
        System.out.print("Enter historical start date (yyyy-MM-dd): ");
        Date startDate = getDateInput();
        
        System.out.print("Enter historical end date (yyyy-MM-dd): ");
        Date endDate = getDateInput();
        
        System.out.print("Enter number of future days to project: ");
        int futureDays = getChoice();
        
        if (futureDays <= 0) {
            System.out.println("Invalid number of days.");
            return;
        }
        
        Expenditure[] expenditures = getExpenditureArray();
        double projection = FinancialAnalysis.projectFutureSpending(expenditures, startDate, endDate, futureDays);
        
        System.out.println("\n--- Future Spending Projection ---");
        System.out.println("Based on period: " + DATE_FORMAT.format(startDate) + " to " + DATE_FORMAT.format(endDate));
        System.out.println("Projected spending for next " + futureDays + " days: $" + String.format("%.2f", projection));
    }
    
    private void calculateBudgetVariance() {
        System.out.println("\n--- Budget Variance Analysis ---");
        System.out.print("Enter planned budget amount: ");
        double plannedBudget = getDoubleInput();
        
        System.out.print("Enter start date (yyyy-MM-dd): ");
        Date startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        Date endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        double variance = FinancialAnalysis.calculateBudgetVariance(expenditures, plannedBudget, startDate, endDate);
        
        System.out.println("\n--- Budget Variance Results ---");
        System.out.println("Period: " + DATE_FORMAT.format(startDate) + " to " + DATE_FORMAT.format(endDate));
        System.out.println("Planned Budget: $" + String.format("%.2f", plannedBudget));
        System.out.println("Actual Spending: $" + String.format("%.2f", plannedBudget + variance));
        System.out.println("Variance: $" + String.format("%.2f", variance));
        
        if (variance > 0.0) {
            System.out.println("Status: OVER BUDGET by $" + String.format("%.2f", variance));
        } else if (variance < 0.0) {
            System.out.println("Status: UNDER BUDGET by $" + String.format("%.2f", Math.abs(variance)));
        } else {
            System.out.println("Status: ON BUDGET");
        }
    }
    
    private void analyzeTrends() {
        System.out.println("\n--- Trend Analysis ---");
        System.out.print("Enter start date (yyyy-MM-dd): ");
        Date startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        Date endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        TrendAnalysis trendAnalysis = FinancialAnalysis.analyzeTrends(expenditures, startDate, endDate);
        
        System.out.println("\n--- Trend Analysis Results ---");
        System.out.println("Period: " + DATE_FORMAT.format(startDate) + " to " + DATE_FORMAT.format(endDate));
        System.out.println("Overall Trend: " + trendAnalysis.getTrendDirection());
        
        List<Double> weeklySpending = trendAnalysis.getWeeklySpending();
        System.out.println("\nWeekly Spending Breakdown:");
        for (int i = 0; i < weeklySpending.size(); i++) {
            System.out.println("Week " + (i + 1) + ": $" + String.format("%.2f", weeklySpending.get(i)));
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
                System.out.println((i + 1) + ". " + summary.getCategory() + ": $" + String.format("%.2f", summary.getTotalAmount()));
            }
        }
    }
    
    private void calculateEfficiencyMetrics() {
        System.out.println("\n--- Efficiency Metrics ---");
        System.out.print("Enter start date (yyyy-MM-dd): ");
        Date startDate = getDateInput();
        
        System.out.print("Enter end date (yyyy-MM-dd): ");
        Date endDate = getDateInput();
        
        Expenditure[] expenditures = getExpenditureArray();
        EfficiencyMetrics metrics = FinancialAnalysis.calculateEfficiencyMetrics(expenditures, startDate, endDate);
        
        System.out.println("\n--- Efficiency Metrics Results ---");
        System.out.println("Period: " + DATE_FORMAT.format(startDate) + " to " + DATE_FORMAT.format(endDate));
        System.out.println("Total Spent: $" + String.format("%.2f", metrics.getTotalSpent()));
        System.out.println("Transaction Count: " + metrics.getTransactionCount());
        System.out.println("Unique Phases: " + metrics.getUniqueVendorCount());
        System.out.println("Average Transaction Amount: $" + String.format("%.2f", metrics.getAverageTransactionAmount()));
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
