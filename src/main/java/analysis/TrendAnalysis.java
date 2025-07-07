package analysis;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents trend analysis results.
 */
public class TrendAnalysis {
    private List<BigDecimal> weeklySpending;
    private FinancialAnalysis.TrendDirection trendDirection;
    
    public TrendAnalysis(List<BigDecimal> weeklySpending, FinancialAnalysis.TrendDirection trendDirection) {
        this.weeklySpending = weeklySpending;
        this.trendDirection = trendDirection;
    }
    
    public List<BigDecimal> getWeeklySpending() { return weeklySpending; }
    public FinancialAnalysis.TrendDirection getTrendDirection() { return trendDirection; }
    
    @Override
    public String toString() {
        return String.format("TrendAnalysis{weeklySpending=%s, trendDirection=%s}", 
                           weeklySpending, trendDirection);
    }
}
