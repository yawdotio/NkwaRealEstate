package analysis;

/**
 * Represents a category spending summary.
 */
public class CategorySummary {
    private String category;
    private double totalAmount;
    
    public CategorySummary(String category, double totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }
    
    public String getCategory() { return category; }
    public double getTotalAmount() { return totalAmount; }
    
    // Backward compatibility
    public String getCategoryId() { return category; }
    
    @Override
    public String toString() {
        return String.format("CategorySummary{category='%s', totalAmount=%.2f}", 
                           category, totalAmount);
    }
}
