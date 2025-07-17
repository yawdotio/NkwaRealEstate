package searchsort;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents search criteria for filtering expenditures.
 */
public class SearchCriteria {
    private String description;
    private String Phase;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String categoryId;
    private String accountId;
    
    public SearchCriteria() {
        // Default constructor
    }
    
    // Getters and setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPhase() { return Phase; }
    public void setPhase(String Phase) { this.Phase = Phase; }
    
    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
    
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    
    // Builder pattern methods for fluent API
    public SearchCriteria withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public SearchCriteria withPhase(String Phase) {
        this.Phase = Phase;
        return this;
    }
    
    public SearchCriteria withAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        return this;
    }
    
    public SearchCriteria withDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }
    
    public SearchCriteria withCategory(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }
    
    public SearchCriteria withAccount(String accountId) {
        this.accountId = accountId;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("SearchCriteria{description='%s', Phase='%s', minAmount=%s, maxAmount=%s, startDate=%s, endDate=%s, categoryId='%s', accountId='%s'}", 
                           description, Phase, minAmount, maxAmount, startDate, endDate, categoryId, accountId);
    }
}
