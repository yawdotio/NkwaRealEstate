package expenditures;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an expenditure record in the system.
 * Links to account and category information.
 */
public class Expenditure {
    private String expenditureId;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String accountId;
    private String categoryId;
    private String vendor;
    private String projectId;
    
    public Expenditure(String expenditureId, String description, BigDecimal amount, LocalDate date, 
                      String accountId, String categoryId, String vendor, String projectId) {
        this.expenditureId = expenditureId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.vendor = vendor;
        this.projectId = projectId;
    }
    
    // Getters and setters
    public String getExpenditureId() { return expenditureId; }
    public void setExpenditureId(String expenditureId) { this.expenditureId = expenditureId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expenditure that = (Expenditure) o;
        return Objects.equals(expenditureId, that.expenditureId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(expenditureId);
    }
    
    @Override
    public String toString() {
        return String.format("Expenditure{id='%s', description='%s', amount=%s, date=%s, account='%s', category='%s', vendor='%s', project='%s'}", 
                           expenditureId, description, amount, date, accountId, categoryId, vendor, projectId);
    }
}
