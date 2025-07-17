package accounts;

import java.io.*;
import java.util.*;

/**
 * Manages all company bank accounts using a HashMap.
 * Responsible for file persistence and account operations.
 */
public class AccountManager {
    private final HashMap<String, Account> accounts;
    private final String filePath = "src/main/resources/accounts.txt";

    public AccountManager() {
        accounts = new HashMap<>();
        loadFromFile();
    }

    /**
     * Adds a new bank account to the system and persists the change.
     */
    public void addAccount(Account account) {
        accounts.put(account.getAccountId(), account);
        saveToFile();
    }

    /**
     * Retrieves an account by ID.
     */
    public Account getAccount(String id) {
        return accounts.get(id);
    }

    /**
     * Returns all bank accounts.
     */
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    /**
     * Updates an existing account.
     */
    public void updateAccount(Account account) {
        if (accounts.containsKey(account.getAccountId())) {
            accounts.put(account.getAccountId(), account);
            saveToFile();
        }
    }

    /**
     * Withdraw funds from an account and update balance.
     */
    public boolean withdrawFromAccount(String id, double amount) {
        Account acc = accounts.get(id);
        if (acc != null && acc.withdraw(amount)) {
            saveToFile();
            return true;
        }
        return false;
    }

    /**
     * Deposit funds into an account.
     */
    public boolean depositToAccount(String id, double amount) {
        Account acc = accounts.get(id);
        if (acc != null) {
            acc.deposit(amount);
            saveToFile();
            return true;
        }
        return false;
    }
    
    /**
     * Removes an account by ID.
     */
    public boolean removeAccount(String accountId) {
        Account removed = accounts.remove(accountId);
        if (removed != null) {
            saveToFile();
            return true;
        }
        return false;
    }
    
    /**
     * Load all account data from file.
     */
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String id = parts[0];
                    String bank = parts[1];
                    double balance = Double.parseDouble(parts[2]);

                    Account acc = new Account(id, bank, balance);

                    if (parts.length == 4 && !parts[3].isEmpty()) {
                        String[] codes = parts[3].split(",");
                        for (String code : codes) {
                            acc.addExpenditure(code);
                        }
                    }

                    accounts.put(id, acc);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
    
    /**
     * Save all account data to file.
     * Format: accountId|bankName|balance|expenditureCode1,expenditureCode2,...
     */
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Account acc : accounts.values()) {
                String line = acc.getAccountId() + "|" +
                              acc.getBankName() + "|" +
                              acc.getBalance() + "|" +
                              String.join(",", acc.getExpenditureCodes());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
}
