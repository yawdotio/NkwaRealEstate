package analysis;

/**
 * Represents efficiency metrics for spending analysis.
 */
public class EfficiencyMetrics {
    private double totalSpent;
    private int transactionCount;
    private int uniquePhaseCount;
    private double averageTransactionAmount;
    
    public EfficiencyMetrics(double totalSpent, int transactionCount, 
                           int uniquePhaseCount, double averageTransactionAmount) {
        this.totalSpent = totalSpent;
        this.transactionCount = transactionCount;
        this.uniquePhaseCount = uniquePhaseCount;
        this.averageTransactionAmount = averageTransactionAmount;
    }
    
    public double getTotalSpent() { return totalSpent; }
    public int getTransactionCount() { return transactionCount; }
    public int getUniquePhaseCount() { return uniquePhaseCount; }
    public double getAverageTransactionAmount() { return averageTransactionAmount; }
    
    @Override
    public String toString() {
        return String.format("EfficiencyMetrics{totalSpent=%.2f, transactionCount=%d, uniquePhaseCount=%d, averageTransactionAmount=%.2f}", 
                           totalSpent, transactionCount, uniquePhaseCount, averageTransactionAmount);
    }
}
