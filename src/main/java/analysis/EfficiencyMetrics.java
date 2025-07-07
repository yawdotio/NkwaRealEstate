package analysis;

import java.math.BigDecimal;

/**
 * Represents efficiency metrics for spending analysis.
 */
public class EfficiencyMetrics {
    private BigDecimal totalSpent;
    private int transactionCount;
    private int uniqueVendorCount;
    private BigDecimal averageTransactionAmount;
    
    public EfficiencyMetrics(BigDecimal totalSpent, int transactionCount, 
                           int uniqueVendorCount, BigDecimal averageTransactionAmount) {
        this.totalSpent = totalSpent;
        this.transactionCount = transactionCount;
        this.uniqueVendorCount = uniqueVendorCount;
        this.averageTransactionAmount = averageTransactionAmount;
    }
    
    public BigDecimal getTotalSpent() { return totalSpent; }
    public int getTransactionCount() { return transactionCount; }
    public int getUniqueVendorCount() { return uniqueVendorCount; }
    public BigDecimal getAverageTransactionAmount() { return averageTransactionAmount; }
    
    @Override
    public String toString() {
        return String.format("EfficiencyMetrics{totalSpent=%s, transactionCount=%d, uniqueVendorCount=%d, averageTransactionAmount=%s}", 
                           totalSpent, transactionCount, uniqueVendorCount, averageTransactionAmount);
    }
}
