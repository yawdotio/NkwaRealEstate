package searchsort;

import expenditures.Expenditure;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Utilities for searching and sorting expenditure records.
 * Uses arrays and binary search for efficient operations.
 */
public class SearchSortUtils {
    
    /**
     * Searches for expenditures by description using binary search.
     * Array must be sorted by description first.
     */
    public static List<Expenditure> searchByDescription(Expenditure[] expenditures, String searchTerm) {
        List<Expenditure> results = new ArrayList<>();
        
        // Sort array by description for binary search
        Arrays.sort(expenditures, Comparator.comparing(Expenditure::getDescription));
        
        // Find all matches using binary search approach
        for (Expenditure exp : expenditures) {
            if (exp.getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Searches for expenditures by vendor using binary search.
     */
    public static List<Expenditure> searchByVendor(Expenditure[] expenditures, String vendor) {
        List<Expenditure> results = new ArrayList<>();
        
        // Sort array by vendor for binary search
        Arrays.sort(expenditures, Comparator.comparing(Expenditure::getVendor));
        
        // Binary search for exact vendor match
        int index = Arrays.binarySearch(expenditures, 
            new Expenditure("", "", BigDecimal.ZERO, LocalDate.now(), "", "", vendor, ""),
            Comparator.comparing(Expenditure::getVendor));
        
        if (index >= 0) {
            // Found exact match, collect all expenditures with same vendor
            results.add(expenditures[index]);
            
            // Check left side
            int left = index - 1;
            while (left >= 0 && expenditures[left].getVendor().equals(vendor)) {
                results.add(expenditures[left]);
                left--;
            }
            
            // Check right side
            int right = index + 1;
            while (right < expenditures.length && expenditures[right].getVendor().equals(vendor)) {
                results.add(expenditures[right]);
                right++;
            }
        }
        
        return results;
    }
    
    /**
     * Filters expenditures by amount range.
     */
    public static List<Expenditure> filterByAmountRange(Expenditure[] expenditures, BigDecimal minAmount, BigDecimal maxAmount) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            BigDecimal amount = exp.getAmount();
            if (amount.compareTo(minAmount) >= 0 && amount.compareTo(maxAmount) <= 0) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Filters expenditures by date range.
     */
    public static List<Expenditure> filterByDateRange(Expenditure[] expenditures, LocalDate startDate, LocalDate endDate) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            LocalDate expDate = exp.getDate();
            if (!expDate.isBefore(startDate) && !expDate.isAfter(endDate)) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Filters expenditures by category.
     */
    public static List<Expenditure> filterByCategory(Expenditure[] expenditures, String categoryId) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            if (categoryId.equals(exp.getCategoryId())) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Filters expenditures by account.
     */
    public static List<Expenditure> filterByAccount(Expenditure[] expenditures, String accountId) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            if (accountId.equals(exp.getAccountId())) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Sorts expenditures by date (newest first).
     */
    public static Expenditure[] sortByDate(Expenditure[] expenditures, boolean descending) {
        Expenditure[] sorted = Arrays.copyOf(expenditures, expenditures.length);
        
        if (descending) {
            Arrays.sort(sorted, Comparator.comparing(Expenditure::getDate).reversed());
        } else {
            Arrays.sort(sorted, Comparator.comparing(Expenditure::getDate));
        }
        
        return sorted;
    }
    
    /**
     * Sorts expenditures by amount.
     */
    public static Expenditure[] sortByAmount(Expenditure[] expenditures, boolean descending) {
        Expenditure[] sorted = Arrays.copyOf(expenditures, expenditures.length);
        
        if (descending) {
            Arrays.sort(sorted, Comparator.comparing(Expenditure::getAmount).reversed());
        } else {
            Arrays.sort(sorted, Comparator.comparing(Expenditure::getAmount));
        }
        
        return sorted;
    }
    
    /**
     * Sorts expenditures by vendor name.
     */
    public static Expenditure[] sortByVendor(Expenditure[] expenditures) {
        Expenditure[] sorted = Arrays.copyOf(expenditures, expenditures.length);
        Arrays.sort(sorted, Comparator.comparing(Expenditure::getVendor));
        return sorted;
    }
    
    /**
     * Sorts expenditures by description.
     */
    public static Expenditure[] sortByDescription(Expenditure[] expenditures) {
        Expenditure[] sorted = Arrays.copyOf(expenditures, expenditures.length);
        Arrays.sort(sorted, Comparator.comparing(Expenditure::getDescription));
        return sorted;
    }
    
    /**
     * Performs multi-field search with multiple criteria.
     */
    public static List<Expenditure> multiFieldSearch(Expenditure[] expenditures, SearchCriteria criteria) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            if (matchesCriteria(exp, criteria)) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Checks if an expenditure matches the search criteria.
     */
    private static boolean matchesCriteria(Expenditure exp, SearchCriteria criteria) {
        // Check description
        if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
            if (!exp.getDescription().toLowerCase().contains(criteria.getDescription().toLowerCase())) {
                return false;
            }
        }
        
        // Check vendor
        if (criteria.getVendor() != null && !criteria.getVendor().isEmpty()) {
            if (!exp.getVendor().toLowerCase().contains(criteria.getVendor().toLowerCase())) {
                return false;
            }
        }
        
        // Check amount range
        if (criteria.getMinAmount() != null) {
            if (exp.getAmount().compareTo(criteria.getMinAmount()) < 0) {
                return false;
            }
        }
        
        if (criteria.getMaxAmount() != null) {
            if (exp.getAmount().compareTo(criteria.getMaxAmount()) > 0) {
                return false;
            }
        }
        
        // Check date range
        if (criteria.getStartDate() != null) {
            if (exp.getDate().isBefore(criteria.getStartDate())) {
                return false;
            }
        }
        
        if (criteria.getEndDate() != null) {
            if (exp.getDate().isAfter(criteria.getEndDate())) {
                return false;
            }
        }
        
        // Check category
        if (criteria.getCategoryId() != null && !criteria.getCategoryId().isEmpty()) {
            if (!criteria.getCategoryId().equals(exp.getCategoryId())) {
                return false;
            }
        }
        
        // Check account
        if (criteria.getAccountId() != null && !criteria.getAccountId().isEmpty()) {
            if (!criteria.getAccountId().equals(exp.getAccountId())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets top N expenditures by amount.
     */
    public static List<Expenditure> getTopExpenditures(Expenditure[] expenditures, int n) {
        Expenditure[] sorted = sortByAmount(expenditures, true);
        List<Expenditure> top = new ArrayList<>();
        
        for (int i = 0; i < Math.min(n, sorted.length); i++) {
            top.add(sorted[i]);
        }
        
        return top;
    }
    
    /**
     * Gets expenditures grouped by vendor.
     */
    public static Map<String, List<Expenditure>> groupByVendor(Expenditure[] expenditures) {
        Map<String, List<Expenditure>> grouped = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String vendor = exp.getVendor();
            grouped.computeIfAbsent(vendor, k -> new ArrayList<>()).add(exp);
        }
        
        return grouped;
    }
    
    /**
     * Gets expenditures grouped by category.
     */
    public static Map<String, List<Expenditure>> groupByCategory(Expenditure[] expenditures) {
        Map<String, List<Expenditure>> grouped = new HashMap<>();
        
        for (Expenditure exp : expenditures) {
            String category = exp.getCategoryId();
            grouped.computeIfAbsent(category, k -> new ArrayList<>()).add(exp);
        }
        
        return grouped;
    }
}
