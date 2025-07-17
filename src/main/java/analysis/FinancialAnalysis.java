package analysis;

import expenditures.Expenditure;
import accounts.Account;
import accounts.AccountManager;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Financial analysis utilities for forecasting and burn rate analysis.
 * Updated to use NkwaExpenditureSystem structure with Date and double types.
 */
public class FinancialAnalysis {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    
    public enum TrendDirection {
        INCREASING, DECREASING, STABLE
    }
    
    /**
     * Calculates the burn rate (spending rate) over a period.
     */
    public static double calculateBurnRate(Expenditure[] expenditures, Date startDate, Date endDate) {
        double totalSpent = 0.0;
        
        for (Expenditure exp : expenditures) {
            Date expDate = exp.getDate();
            if (!expDate.before(startDate) && !expDate.after(endDate)) {
                totalSpent += exp.getAmount();
            }
        }
        
        long daysBetween = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        if (daysBetween == 0) {
            return totalSpent;
        }
        
        return totalSpent / daysBetween;
    }
    
    /**
     * Analyzes expenditures by category.
     */
    public static Map<String, CategoryAnalysis> analyzeByCategory(Expenditure[] expenditures) {
        Map<String, CategoryAnalysis> categoryAnalysis = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String category = exp.getCategory();
            CategoryAnalysis analysis = categoryAnalysis.get(category);
            
            if (analysis == null) {
                analysis = new CategoryAnalysis(category);
                categoryAnalysis.put(category, analysis);
            }
            
            analysis.addExpenditure(exp);
        }
        
        return categoryAnalysis;
    }
    
    /**
     * Calculates monthly spending for a specific year.
     */
    public static Map<String, Double> calculateMonthlySpending(Expenditure[] expenditures, int year) {
        Map<String, Double> monthlySpending = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        
        // Initialize all months
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthlySpending.put(month, 0.0);
        }
        
        for (Expenditure exp : expenditures) {
            calendar.setTime(exp.getDate());
            int expYear = calendar.get(Calendar.YEAR);
            
            if (expYear == year) {
                int month = calendar.get(Calendar.MONTH);
                String monthName = months[month];
                Double currentTotal = monthlySpending.get(monthName);
                monthlySpending.put(monthName, currentTotal + exp.getAmount());
            }
        }
        
        return monthlySpending;
    }
    
    /**
     * Projects future spending based on historical data.
     */
    public static double projectFutureSpending(Expenditure[] expenditures, Date startDate, Date endDate, int futureDays) {
        double burnRate = calculateBurnRate(expenditures, startDate, endDate);
        return burnRate * futureDays;
    }
    
    /**
     * Calculates budget variance.
     */
    public static double calculateBudgetVariance(Expenditure[] expenditures, double plannedBudget, Date startDate, Date endDate) {
        double totalSpent = 0.0;
        
        for (Expenditure exp : expenditures) {
            Date expDate = exp.getDate();
            if (!expDate.before(startDate) && !expDate.after(endDate)) {
                totalSpent += exp.getAmount();
            }
        }
        
        return totalSpent - plannedBudget;
    }
    
    /**
     * Analyzes trends in spending over time.
     */
    public static TrendAnalysis analyzeTrends(Expenditure[] expenditures, Date startDate, Date endDate) {
        List<Double> weeklySpending = new ArrayList<>();
        
        // Calculate weekly spending
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        while (cal.getTime().before(endDate)) {
            Date weekStart = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date weekEnd = cal.getTime().after(endDate) ? endDate : cal.getTime();
            
            double weeklyAmount = 0.0;
            for (Expenditure exp : expenditures) {
                Date expDate = exp.getDate();
                if (!expDate.before(weekStart) && expDate.before(weekEnd)) {
                    weeklyAmount += exp.getAmount();
                }
            }
            
            weeklySpending.add(weeklyAmount);
        }
        
        // Determine trend direction
        TrendDirection trendDirection = TrendDirection.STABLE;
        if (weeklySpending.size() >= 2) {
            double firstWeek = weeklySpending.get(0);
            double lastWeek = weeklySpending.get(weeklySpending.size() - 1);
            
            if (lastWeek > firstWeek * 1.1) {
                trendDirection = TrendDirection.INCREASING;
            } else if (lastWeek < firstWeek * 0.9) {
                trendDirection = TrendDirection.DECREASING;
            }
        }
        
        return new TrendAnalysis(weeklySpending, trendDirection);
    }
    
    /**
     * Gets top spending categories.
     */
    public static List<CategorySummary> getTopSpendingCategories(Expenditure[] expenditures, int topN) {
        Map<String, Double> categoryTotals = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String category = exp.getCategory();
            Double currentTotal = categoryTotals.getOrDefault(category, 0.0);
            categoryTotals.put(category, currentTotal + exp.getAmount());
        }
        
        List<CategorySummary> summaries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            summaries.add(new CategorySummary(entry.getKey(), entry.getValue()));
        }
        
        // Sort by total amount (descending)
        summaries.sort((a, b) -> Double.compare(b.getTotalAmount(), a.getTotalAmount()));
        
        // Return top N
        return summaries.subList(0, Math.min(topN, summaries.size()));
    }
    
    /**
     * Calculates efficiency metrics.
     */
    public static EfficiencyMetrics calculateEfficiencyMetrics(Expenditure[] expenditures, Date startDate, Date endDate) {
        double totalSpent = 0.0;
        int transactionCount = 0;
        Set<String> uniquePhases = new HashSet<>();
        
        for (Expenditure exp : expenditures) {
            Date expDate = exp.getDate();
            if (!expDate.before(startDate) && !expDate.after(endDate)) {
                totalSpent += exp.getAmount();
                transactionCount++;
                uniquePhases.add(exp.getPhase());
            }
        }
        
        double averageTransactionAmount = transactionCount > 0 ? totalSpent / transactionCount : 0.0;
        
        return new EfficiencyMetrics(totalSpent, transactionCount, uniquePhases.size(), averageTransactionAmount);
    }
}
