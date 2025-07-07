package accounts;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Manages bank accounts using Map data structure.
 * Handles account creation, retrieval, and persistence.
 */
public class AccountManager {
    private Map<String, Account> accounts;
    private static final String ACCOUNTS_FILE = "src/main/resources/accounts.txt";
    
    public AccountManager() {
        this.accounts = new HashMap<>();
        loadAccounts();
    }
    
    /**
     * Adds a new account to the system.
     */
    public void addAccount(Account account) {
        accounts.put(account.getAccountId(), account);
        saveAccounts();
    }
    
    /**
     * Retrieves an account by ID.
     */
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }
    
    /**
     * Returns all accounts.
     */
    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }
    
    /**
     * Updates an existing account.
     */
    public void updateAccount(Account account) {
        if (accounts.containsKey(account.getAccountId())) {
            accounts.put(account.getAccountId(), account);
            saveAccounts();
        }
    }
    
    /**
     * Removes an account by ID.
     */
    public boolean removeAccount(String accountId) {
        Account removed = accounts.remove(accountId);
        if (removed != null) {
            saveAccounts();
            return true;
        }
        return false;
    }
    
    /**
     * Loads accounts from file.
     */
    private void loadAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Account account = parseAccount(line);
                    if (account != null) {
                        accounts.put(account.getAccountId(), account);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }
    
    /**
     * Saves accounts to file.
     */
    private void saveAccounts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts.values()) {
                writer.println(formatAccount(account));
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }
    
    /**
     * Parses a line from the file into an Account object.
     * Format: accountId,accountName,accountType,balance,bankName
     */
    private Account parseAccount(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                String accountId = parts[0].trim();
                String accountName = parts[1].trim();
                String accountType = parts[2].trim();
                BigDecimal balance = new BigDecimal(parts[3].trim());
                String bankName = parts[4].trim();
                
                return new Account(accountId, accountName, accountType, balance, bankName);
            }
        } catch (Exception e) {
            System.err.println("Error parsing account line: " + line);
        }
        return null;
    }
    
    /**
     * Formats an Account object for file storage.
     */
    private String formatAccount(Account account) {
        return String.format("%s,%s,%s,%s,%s",
                           account.getAccountId(),
                           account.getAccountName(),
                           account.getAccountType(),
                           account.getBalance().toString(),
                           account.getBankName());
    }
    
    /**
     * Gets accounts with balance below a threshold.
     */
    public List<Account> getAccountsBelowThreshold(BigDecimal threshold) {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getBalance().compareTo(threshold) < 0) {
                result.add(account);
            }
        }
        return result;
    }
}
