package analysis;

import expenditures.Expenditure;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Analysis data for a specific Phase.
 */
public class PhaseAnalysis {
    private String Phase;
    private List<Expenditure> expenditures;
    private double totalAmount;
    private int transactionCount;
    private double averageAmount;
    private Date firstTransaction;
    private Date lastTransaction;
    
    public PhaseAnalysis(String Phase) {
        this.Phase = Phase;
        this.expenditures = new ArrayList<>();
        this.totalAmount = 0.0;
        this.transactionCount = 0;
        this.averageAmount = 0.0;
    }
    
    public void addExpenditure(Expenditure expenditure) {
        expenditures.add(expenditure);
        totalAmount += expenditure.getAmount();
        transactionCount++;
        
        if (firstTransaction == null || expenditure.getDate().before(firstTransaction)) {
            firstTransaction = expenditure.getDate();
        }
        
        if (lastTransaction == null || expenditure.getDate().after(lastTransaction)) {
            lastTransaction = expenditure.getDate();
        }
        
        recalculateAverage();
    }
    
    private void recalculateAverage() {
        if (transactionCount > 0) {
            averageAmount = totalAmount / transactionCount;
        }
    }
    
    // Getters
    public String getPhase() { return Phase; }
    public List<Expenditure> getExpenditures() { return new ArrayList<>(expenditures); }
    public double getTotalAmount() { return totalAmount; }
    public int getTransactionCount() { return transactionCount; }
    public double getAverageAmount() { return averageAmount; }
    public Date getFirstTransaction() { return firstTransaction; }
    public Date getLastTransaction() { return lastTransaction; }
    
    @Override
    public String toString() {
        return String.format("PhaseAnalysis{Phase='%s', totalAmount=%.2f, transactionCount=%d, averageAmount=%.2f}", 
                           Phase, totalAmount, transactionCount, averageAmount);
    }
}
