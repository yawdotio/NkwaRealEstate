package analysis;

import expenditures.Expenditure;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Financial analysis utilities for forecasting and burn rate analysis.
 * Uses arrays and maps for efficient data processing.
 */
public class FinancialAnalysis {
    
    /**
     * Calculates the burn rate (spending rate) over a period.
     */
    public static BigDecimal calculateBurnRate(Expenditure[] expenditures, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalSpent = BigDecimal.ZERO;
        
        for (Expenditure exp : expenditures) {
            LocalDate expDate = exp.getDate();
            if (!expDate.isBefore(startDate) && !expDate.isAfter(endDate)) {
                totalSpent = totalSpent.add(exp.getAmount());
            }
        }
        
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween == 0) {
            return totalSpent;
        }
        
        return totalSpent.divide(BigDecimal.valueOf(daysBetween), 2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Calculates monthly spending analysis.
     */
    public static Map<String, BigDecimal> calculateMonthlySpending(Expenditure[] expenditures, int year) {
        Map<String, BigDecimal> monthlySpending = new HashMap<>();
        
        // Initialize all months with zero
        for (int month = 1; month <= 12; month++) {
            monthlySpending.put(String.format("%d-%02d", year, month), BigDecimal.ZERO);
        }
        
        for (Expenditure exp : expenditures) {
            LocalDate expDate = exp.getDate();
            if (expDate.getYear() == year) {
                String monthKey = String.format("%d-%02d", year, expDate.getMonthValue());
                BigDecimal currentAmount = monthlySpending.get(monthKey);
                monthlySpending.put(monthKey, currentAmount.add(exp.getAmount()));
            }
        }
        
        return monthlySpending;
    }
    
    /**
     * Analyzes spending by category.
     */
    public static Map<String, CategoryAnalysis> analyzeByCategoryIds(Expenditure[] expenditures) {
        Map<String, CategoryAnalysis> categoryAnalysis = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String categoryId = exp.getCategoryId();
            CategoryAnalysis analysis = categoryAnalysis.get(categoryId);
            
            if (analysis == null) {
                analysis = new CategoryAnalysis(categoryId);
                categoryAnalysis.put(categoryId, analysis);
            }
            
            analysis.addExpenditure(exp);
        }
        
        return categoryAnalysis;
    }
    
    /**
     * Analyzes spending by vendor.
     */
    public static Map<String, VendorAnalysis> analyzeByVendor(Expenditure[] expenditures) {
        Map<String, VendorAnalysis> vendorAnalysis = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String vendor = exp.getVendor();
            VendorAnalysis analysis = vendorAnalysis.get(vendor);
            
            if (analysis == null) {
                analysis = new VendorAnalysis(vendor);
                vendorAnalysis.put(vendor, analysis);
            }
            
            analysis.addExpenditure(exp);
        }
        
        return vendorAnalysis;
    }
    
    /**
     * Projects future spending based on historical data.
     */
    public static BigDecimal projectFutureSpending(Expenditure[] expenditures, LocalDate startDate, LocalDate endDate, int futureDays) {
        BigDecimal dailyAverage = calculateBurnRate(expenditures, startDate, endDate);
        return dailyAverage.multiply(BigDecimal.valueOf(futureDays));
    }
    
    /**
     * Calculates budget variance (actual vs planned).
     */
    public static BigDecimal calculateBudgetVariance(Expenditure[] expenditures, BigDecimal plannedBudget, LocalDate startDate, LocalDate endDate) {
        BigDecimal actualSpending = BigDecimal.ZERO;
        
        for (Expenditure exp : expenditures) {
            LocalDate expDate = exp.getDate();
            if (!expDate.isBefore(startDate) && !expDate.isAfter(endDate)) {
                actualSpending = actualSpending.add(exp.getAmount());
            }
        }
        
        return actualSpending.subtract(plannedBudget);
    }
    
    /**
     * Identifies spending trends over time.
     */
    public static TrendAnalysis analyzeTrends(Expenditure[] expenditures, LocalDate startDate, LocalDate endDate) {
        // Sort expenditures by date
        Expenditure[] sortedExpenditures = Arrays.copyOf(expenditures, expenditures.length);
        Arrays.sort(sortedExpenditures, Comparator.comparing(Expenditure::getDate));
        
        List<BigDecimal> weeklySpending = new ArrayList<>();
        LocalDate currentWeekStart = startDate;
        
        while (!currentWeekStart.isAfter(endDate)) {
            LocalDate weekEnd = currentWeekStart.plusDays(6);
            if (weekEnd.isAfter(endDate)) {
                weekEnd = endDate;
            }
            
            BigDecimal weeklyAmount = BigDecimal.ZERO;
            for (Expenditure exp : sortedExpenditures) {
                LocalDate expDate = exp.getDate();
                if (!expDate.isBefore(currentWeekStart) && !expDate.isAfter(weekEnd)) {
                    weeklyAmount = weeklyAmount.add(exp.getAmount());
                }
            }
            
            weeklySpending.add(weeklyAmount);
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }
        
        return new TrendAnalysis(weeklySpending, calculateTrend(weeklySpending));
    }
    
    /**
     * Calculates the top spending categories.
     */
    public static List<CategorySummary> getTopSpendingCategories(Expenditure[] expenditures, int topN) {
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String categoryId = exp.getCategoryId();
            BigDecimal currentTotal = categoryTotals.getOrDefault(categoryId, BigDecimal.ZERO);
            categoryTotals.put(categoryId, currentTotal.add(exp.getAmount()));
        }
        
        List<CategorySummary> summaries = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            summaries.add(new CategorySummary(entry.getKey(), entry.getValue()));
        }
        
        // Sort by amount descending
        summaries.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));
        
        return summaries.subList(0, Math.min(topN, summaries.size()));
    }
    
    /**
     * Calculates spending efficiency metrics.
     */
    public static EfficiencyMetrics calculateEfficiencyMetrics(Expenditure[] expenditures, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalSpent = BigDecimal.ZERO;
        int transactionCount = 0;
        Set<String> uniqueVendors = new HashSet<>();
        
        for (Expenditure exp : expenditures) {
            LocalDate expDate = exp.getDate();
            if (!expDate.isBefore(startDate) && !expDate.isAfter(endDate)) {
                totalSpent = totalSpent.add(exp.getAmount());
                transactionCount++;
                uniqueVendors.add(exp.getVendor());
            }
        }
        
        BigDecimal averageTransactionAmount = transactionCount > 0 ? 
            totalSpent.divide(BigDecimal.valueOf(transactionCount), 2, java.math.RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        return new EfficiencyMetrics(totalSpent, transactionCount, uniqueVendors.size(), averageTransactionAmount);
    }
    
    /**
     * Calculates simple trend direction from a list of values.
     */
    private static TrendDirection calculateTrend(List<BigDecimal> values) {
        if (values.size() < 2) {
            return TrendDirection.STABLE;
        }
        
        BigDecimal firstHalfSum = BigDecimal.ZERO;
        BigDecimal secondHalfSum = BigDecimal.ZERO;
        
        int midPoint = values.size() / 2;
        
        for (int i = 0; i < midPoint; i++) {
            firstHalfSum = firstHalfSum.add(values.get(i));
        }
        
        for (int i = midPoint; i < values.size(); i++) {
            secondHalfSum = secondHalfSum.add(values.get(i));
        }
        
        BigDecimal avgFirst = firstHalfSum.divide(BigDecimal.valueOf(midPoint), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal avgSecond = secondHalfSum.divide(BigDecimal.valueOf(values.size() - midPoint), 2, java.math.RoundingMode.HALF_UP);
        
        BigDecimal difference = avgSecond.subtract(avgFirst);
        BigDecimal threshold = avgFirst.multiply(BigDecimal.valueOf(0.1)); // 10% threshold
        
        if (difference.compareTo(threshold) > 0) {
            return TrendDirection.INCREASING;
        } else if (difference.compareTo(threshold.negate()) < 0) {
            return TrendDirection.DECREASING;
        } else {
            return TrendDirection.STABLE;
        }
    }
    
    public enum TrendDirection {
        INCREASING, DECREASING, STABLE
    }
}
