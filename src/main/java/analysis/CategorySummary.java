package analysis;

import java.math.BigDecimal;

/**
 * Represents a category spending summary.
 */
public class CategorySummary {
    private String categoryId;
    private BigDecimal totalAmount;
    
    public CategorySummary(String categoryId, BigDecimal totalAmount) {
        this.categoryId = categoryId;
        this.totalAmount = totalAmount;
    }
    
    public String getCategoryId() { return categoryId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    
    @Override
    public String toString() {
        return String.format("CategorySummary{categoryId='%s', totalAmount=%s}", 
                           categoryId, totalAmount);
    }
}
