package tracker;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a balance alert for an account.
 */
public class BalanceAlert {
    private String alertId;
    private String accountId;
    private BigDecimal currentBalance;
    private BigDecimal threshold;
    private Date alertDate;
    private boolean isRead;
    
    public BalanceAlert(String alertId, String accountId, BigDecimal currentBalance, 
                       BigDecimal threshold, Date alertDate, boolean isRead) {
        this.alertId = alertId;
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.threshold = threshold;
        this.alertDate = alertDate;
        this.isRead = isRead;
    }
    
    // Getters and setters
    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    
    public BigDecimal getThreshold() { return threshold; }
    public void setThreshold(BigDecimal threshold) { this.threshold = threshold; }
    
    public Date getAlertDate() { return alertDate; }
    public void setAlertDate(Date alertDate) { this.alertDate = alertDate; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public String getMessage() {
        return String.format("Account %s balance (%s) is below threshold (%s)", 
                           accountId, currentBalance, threshold);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceAlert that = (BalanceAlert) o;
        return Objects.equals(alertId, that.alertId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(alertId);
    }
    
    @Override
    public String toString() {
        return String.format("BalanceAlert{id='%s', accountId='%s', currentBalance=%s, threshold=%s, date=%s, read=%b}", 
                           alertId, accountId, currentBalance, threshold, alertDate, isRead);
    }
}
