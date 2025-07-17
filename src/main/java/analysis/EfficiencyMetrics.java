package analysis;

/**
 * Represents efficiency metrics for spending analysis.
 */
public class EfficiencyMetrics {
    private double totalSpent;
    private int transactionCount;
    private int uniqueVendorCount;
    private double averageTransactionAmount;
    
    public EfficiencyMetrics(double totalSpent, int transactionCount, 
                           int uniqueVendorCount, double averageTransactionAmount) {
        this.totalSpent = totalSpent;
        this.transactionCount = transactionCount;
        this.uniqueVendorCount = uniqueVendorCount;
        this.averageTransactionAmount = averageTransactionAmount;
    }
    
    public double getTotalSpent() { return totalSpent; }
    public int getTransactionCount() { return transactionCount; }
    public int getUniqueVendorCount() { return uniqueVendorCount; }
    public double getAverageTransactionAmount() { return averageTransactionAmount; }
    
    @Override
    public String toString() {
        return String.format("EfficiencyMetrics{totalSpent=%.2f, transactionCount=%d, uniqueVendorCount=%d, averageTransactionAmount=%.2f}", 
                           totalSpent, transactionCount, uniqueVendorCount, averageTransactionAmount);
    }
}
