package tracker;

import java.math.BigDecimal;

/**
 * Represents balance statistics for tracked accounts.
 */
public class BalanceStatistics {
    private BigDecimal minBalance;
    private BigDecimal maxBalance;
    private BigDecimal averageBalance;
    private int accountCount;
    
    public BalanceStatistics(BigDecimal minBalance, BigDecimal maxBalance, 
                           BigDecimal averageBalance, int accountCount) {
        this.minBalance = minBalance;
        this.maxBalance = maxBalance;
        this.averageBalance = averageBalance;
        this.accountCount = accountCount;
    }
    
    // Getters and setters
    public BigDecimal getMinBalance() { return minBalance; }
    public void setMinBalance(BigDecimal minBalance) { this.minBalance = minBalance; }
    
    public BigDecimal getMaxBalance() { return maxBalance; }
    public void setMaxBalance(BigDecimal maxBalance) { this.maxBalance = maxBalance; }
    
    public BigDecimal getAverageBalance() { return averageBalance; }
    public void setAverageBalance(BigDecimal averageBalance) { this.averageBalance = averageBalance; }
    
    public int getAccountCount() { return accountCount; }
    public void setAccountCount(int accountCount) { this.accountCount = accountCount; }
    
    @Override
    public String toString() {
        return String.format("BalanceStatistics{min=%s, max=%s, average=%s, count=%d}", 
                           minBalance, maxBalance, averageBalance, accountCount);
    }
}
