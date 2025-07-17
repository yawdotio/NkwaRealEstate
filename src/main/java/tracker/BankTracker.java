package tracker;

import accounts.Account;
import java.math.BigDecimal;
import java.util.*;

/**
 * Bank tracker using Min Heap to track accounts with lowest balances.
 * Provides alerts when account balances fall below thresholds.
 */
public class BankTracker {
    private PriorityQueue<Account> minHeap;
    private Map<String, BigDecimal> alertThresholds;
    private List<BalanceAlert> alerts;
    
    public BankTracker() {
        // Min heap ordered by balance (lowest first)
        this.minHeap = new PriorityQueue<>(Comparator.comparing(Account::getBalance));
        this.alertThresholds = new HashMap<>();
        this.alerts = new ArrayList<>();
    }
    
    /**
     * Adds an account to the tracker.
     */
    public void addAccount(Account account) {
        minHeap.offer(account);
        checkAndCreateAlert(account);
    }
    
    /**
     * Updates an account's balance and repositions in heap.
     */
    public void updateAccount(Account account) {
        // Remove and re-add to maintain heap property
        minHeap.remove(account);
        minHeap.offer(account);
        checkAndCreateAlert(account);
    }
    
    /**
     * Removes an account from tracking.
     */
    public void removeAccount(Account account) {
        minHeap.remove(account);
        alertThresholds.remove(account.getAccountId());
    }
    
    /**
     * Gets the account with the lowest balance.
     */
    public Account getLowestBalanceAccount() {
        return minHeap.peek();
    }
    
    /**
     * Gets the N accounts with the lowest balances.
     */
    public List<Account> getLowestBalanceAccounts(int n) {
        List<Account> result = new ArrayList<>();
        List<Account> temp = new ArrayList<>();
        
        // Extract min elements and store temporarily
        for (int i = 0; i < n && !minHeap.isEmpty(); i++) {
            Account account = minHeap.poll();
            result.add(account);
            temp.add(account);
        }
        
        // Restore heap
        for (Account account : temp) {
            minHeap.offer(account);
        }
        
        return result;
    }
    
    /**
     * Sets an alert threshold for an account.
     */
    public void setAlertThreshold(String accountId, BigDecimal threshold) {
        alertThresholds.put(accountId, threshold);
        
        // Check if current balance is below threshold
        for (Account account : minHeap) {
            if (account.getAccountId().equals(accountId)) {
                checkAndCreateAlert(account);
                break;
            }
        }
    }
    
    /**
     * Gets the alert threshold for an account.
     */
    public BigDecimal getAlertThreshold(String accountId) {
        return alertThresholds.get(accountId);
    }
    
    /**
     * Gets all accounts below their alert thresholds.
     */
    public List<Account> getAccountsBelowThreshold() {
        List<Account> result = new ArrayList<>();
        
        for (Account account : minHeap) {
            BigDecimal threshold = alertThresholds.get(account.getAccountId());
            if (threshold != null && account.getBalance() < threshold.doubleValue()) {
                result.add(account);
            }
        }
        
        return result;
    }
    
    /**
     * Gets all active alerts.
     */
    public List<BalanceAlert> getAlerts() {
        return new ArrayList<>(alerts);
    }
    
    /**
     * Gets unread alerts.
     */
    public List<BalanceAlert> getUnreadAlerts() {
        List<BalanceAlert> unread = new ArrayList<>();
        for (BalanceAlert alert : alerts) {
            if (!alert.isRead()) {
                unread.add(alert);
            }
        }
        return unread;
    }
    
    /**
     * Marks an alert as read.
     */
    public void markAlertAsRead(String alertId) {
        for (BalanceAlert alert : alerts) {
            if (alert.getAlertId().equals(alertId)) {
                alert.setRead(true);
                break;
            }
        }
    }
    
    /**
     * Clears all read alerts.
     */
    public void clearReadAlerts() {
        alerts.removeIf(BalanceAlert::isRead);
    }
    
    /**
     * Gets accounts with balance below a specific amount.
     */
    public List<Account> getAccountsBelowAmount(BigDecimal amount) {
        List<Account> result = new ArrayList<>();
        
        for (Account account : minHeap) {
            if (account.getBalance() < amount.doubleValue()) {
                result.add(account);
            }
        }
        
        return result;
    }
    
    /**
     * Gets the total balance across all tracked accounts.
     */
    public BigDecimal getTotalBalance() {
        BigDecimal total = BigDecimal.ZERO;
        for (Account account : minHeap) {
            total = total.add(BigDecimal.valueOf(account.getBalance()));
        }
        return total;
    }
    
    /**
     * Gets the average balance across all tracked accounts.
     */
    public BigDecimal getAverageBalance() {
        if (minHeap.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return getTotalBalance().divide(BigDecimal.valueOf(minHeap.size()), 2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Gets statistics about tracked accounts.
     */
    public BalanceStatistics getBalanceStatistics() {
        if (minHeap.isEmpty()) {
            return new BalanceStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0);
        }
        
        BigDecimal min = BigDecimal.valueOf(getLowestBalanceAccount().getBalance());
        BigDecimal max = BigDecimal.valueOf(Collections.max(minHeap, Comparator.comparing(Account::getBalance)).getBalance());
        BigDecimal average = getAverageBalance();
        int count = minHeap.size();
        
        return new BalanceStatistics(min, max, average, count);
    }
    
    /**
     * Checks if an account balance is below threshold and creates alert if needed.
     */
    private void checkAndCreateAlert(Account account) {
        BigDecimal threshold = alertThresholds.get(account.getAccountId());
        if (threshold != null && account.getBalance() < threshold.doubleValue()) {
            // Check if we already have an unread alert for this account
            boolean hasUnreadAlert = alerts.stream()
                .anyMatch(alert -> alert.getAccountId().equals(account.getAccountId()) && !alert.isRead());
            
            if (!hasUnreadAlert) {
                BalanceAlert alert = new BalanceAlert(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    BigDecimal.valueOf(account.getBalance()),
                    threshold,
                    new Date(),
                    false
                );
                alerts.add(alert);
            }
        }
    }
}
