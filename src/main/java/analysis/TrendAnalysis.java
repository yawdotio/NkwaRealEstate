package analysis;

import java.util.List;

/**
 * Represents trend analysis results.
 */
public class TrendAnalysis {
    private List<Double> weeklySpending;
    private FinancialAnalysis.TrendDirection trendDirection;
    
    public TrendAnalysis(List<Double> weeklySpending, FinancialAnalysis.TrendDirection trendDirection) {
        this.weeklySpending = weeklySpending;
        this.trendDirection = trendDirection;
    }
    
    public List<Double> getWeeklySpending() { return weeklySpending; }
    public FinancialAnalysis.TrendDirection getTrendDirection() { return trendDirection; }
    
    @Override
    public String toString() {
        return String.format("TrendAnalysis{weeklySpending=%s, trendDirection=%s}", 
                           weeklySpending, trendDirection);
    }
}
