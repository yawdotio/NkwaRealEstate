package analysis;

import expenditures.Expenditure;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Analysis data for a specific category.
 */
public class CategoryAnalysis {
    private String categoryId;
    private List<Expenditure> expenditures;
    private BigDecimal totalAmount;
    private int transactionCount;
    private BigDecimal averageAmount;
    private LocalDate firstTransaction;
    private LocalDate lastTransaction;
    
    public CategoryAnalysis(String categoryId) {
        this.categoryId = categoryId;
        this.expenditures = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        this.transactionCount = 0;
        this.averageAmount = BigDecimal.ZERO;
    }
    
    public void addExpenditure(Expenditure expenditure) {
        expenditures.add(expenditure);
        totalAmount = totalAmount.add(expenditure.getAmount());
        transactionCount++;
        
        if (firstTransaction == null || expenditure.getDate().isBefore(firstTransaction)) {
            firstTransaction = expenditure.getDate();
        }
        
        if (lastTransaction == null || expenditure.getDate().isAfter(lastTransaction)) {
            lastTransaction = expenditure.getDate();
        }
        
        recalculateAverage();
    }
    
    private void recalculateAverage() {
        if (transactionCount > 0) {
            averageAmount = totalAmount.divide(BigDecimal.valueOf(transactionCount), 2, java.math.RoundingMode.HALF_UP);
        }
    }
    
    // Getters
    public String getCategoryId() { return categoryId; }
    public List<Expenditure> getExpenditures() { return new ArrayList<>(expenditures); }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public int getTransactionCount() { return transactionCount; }
    public BigDecimal getAverageAmount() { return averageAmount; }
    public LocalDate getFirstTransaction() { return firstTransaction; }
    public LocalDate getLastTransaction() { return lastTransaction; }
    
    @Override
    public String toString() {
        return String.format("CategoryAnalysis{categoryId='%s', totalAmount=%s, transactionCount=%d, averageAmount=%s}", 
                           categoryId, totalAmount, transactionCount, averageAmount);
    }
}
