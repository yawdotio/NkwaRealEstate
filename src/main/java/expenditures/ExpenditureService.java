package expenditures;

import accounts.Account;
import accounts.AccountManager;
import receipts.Receipt;
import receipts.ReceiptManager;
import java.util.Date;

/**
 * Service class to handle expenditure transactions that update bank account balances.
 * Ensures expenditures are properly tied to bank accounts and balances are updated.
 */
public class ExpenditureService {
    
    private final ExpenditureManager expenditureManager;
    private final AccountManager accountManager;
    private final ReceiptManager receiptManager;
    
    public ExpenditureService(ExpenditureManager expenditureManager, AccountManager accountManager, ReceiptManager receiptManager) {
        this.expenditureManager = expenditureManager;
        this.accountManager = accountManager;
        this.receiptManager = receiptManager;
    }
    
    /**
     * Constructor for backward compatibility - receipts won't be created automatically
     */
    public ExpenditureService(ExpenditureManager expenditureManager, AccountManager accountManager) {
        this.expenditureManager = expenditureManager;
        this.accountManager = accountManager;
        this.receiptManager = null;
    }
    
    /**
     * Creates a new expenditure and updates the associated bank account balance.
     * This method ensures the expenditure is properly tied to a bank account.
     * It also automatically creates a corresponding receipt.
     */
    public boolean createExpenditure(String code, double amount, Date date, String phase, 
                                   String category, String accountId, String receiptPath) {
        
        // Check if account exists and has sufficient funds
        Account account = accountManager.getAccount(accountId);
        if (account == null) {
            System.out.println("Error: Account " + accountId + " not found.");
            return false;
        }
        
        if (account.getBalance() < amount) {
            System.out.println("Error: Insufficient funds in account " + accountId + 
                             ". Available: GHS " + account.getBalance() + 
                             ", Required: GHS " + amount);
            return false;
        }
        
        // Create the expenditure
        Expenditure expenditure = new Expenditure(code, amount, date, phase, category, accountId, receiptPath);
        
        // Withdraw from account and add expenditure reference
        if (accountManager.withdrawFromAccount(accountId, amount)) {
            account.addExpenditure(code);
            expenditureManager.addExpenditure(expenditure);
            
            // Automatically create a corresponding receipt
            createReceiptForExpenditure(expenditure);
            
            System.out.println("Expenditure created successfully:");
            System.out.println("  Code: " + code);
            System.out.println("  Amount: GHS " + amount);
            System.out.println("  Account: " + accountId);
            System.out.println("  New Account Balance: GHS " + account.getBalance());
            if (receiptManager != null) {
                System.out.println("  Receipt created automatically.");
            }
            
            return true;
        } else {
            System.out.println("Error: Failed to withdraw from account " + accountId);
            return false;
        }
    }
    
    /**
     * Gets all expenditures for a specific account.
     */
    public java.util.List<Expenditure> getExpendituresByAccount(String accountId) {
        return expenditureManager.searchByAccountId(accountId);
    }
    
    /**
     * Gets the total amount spent from a specific account.
     */
    public double getTotalSpentFromAccount(String accountId) {
        return expenditureManager.getTotalSpentByAccount(accountId);
    }
    
    /**
     * Validates that an expenditure is properly tied to an account.
     */
    public boolean validateExpenditureAccountLink(String expenditureCode) {
        Expenditure expenditure = expenditureManager.findByCode(expenditureCode);
        if (expenditure == null) {
            return false;
        }
        
        Account account = accountManager.getAccount(expenditure.getAccountId());
        if (account == null) {
            return false;
        }
        
        return account.getExpenditureCodes().contains(expenditureCode);
    }
    
    /**
     * Prints a summary of account balances and expenditures.
     */
    public void printAccountSummary() {
        System.out.println("\n=== ACCOUNT SUMMARY ===");
        for (Account account : accountManager.getAllAccounts()) {
            System.out.println("\nAccount: " + account.getAccountId());
            System.out.println("Bank: " + account.getBankName());
            System.out.println("Current Balance: GHS " + account.getBalance());
            System.out.println("Number of Expenditures: " + account.getExpenditureCodes().size());
            System.out.println("Total Spent: GHS " + getTotalSpentFromAccount(account.getAccountId()));
            
            System.out.println("Recent Expenditures:");
            java.util.List<Expenditure> accountExpenditures = getExpendituresByAccount(account.getAccountId());
            for (Expenditure exp : accountExpenditures) {
                System.out.println("  - " + exp.getCode() + ": GHS " + exp.getAmount() + 
                                 " (" + exp.getCategory() + ")");
            }
        }
    }
    
    /**
     * Creates a receipt for the given expenditure.
     * The receipt is automatically generated with relevant details from the expenditure.
     */
    private void createReceiptForExpenditure(Expenditure expenditure) {
        // Skip receipt creation if receiptManager is not available
        if (receiptManager == null) {
            return;
        }
        
        // Generate a unique receipt ID based on the expenditure code
        String receiptId = "RCP-" + expenditure.getCode();
        
        // Generate a receipt number (could be sequential or based on a pattern)
        String receiptNumber = "R" + System.currentTimeMillis();
        
        // Use expenditure details to create the receipt
        String vendor = "Unknown"; // Default vendor - could be extracted from category or phase
        String description = expenditure.getCategory() + " - " + expenditure.getPhase();
        
        // Create the receipt with PENDING status initially
        Receipt receipt = new Receipt(
            receiptId,
            receiptNumber,
            expenditure.getDate(),
            expenditure.getAmount(),
            vendor,
            description,
            expenditure.getCode(), // Link to expenditure
            Receipt.ReceiptStatus.PENDING,
            expenditure.getReceiptPath()
        );
        
        // Add the receipt to the receipt manager
        receiptManager.addReceipt(receipt);
    }
    
    /**
     * Gets the receipt for a specific expenditure.
     */
    public Receipt getReceiptForExpenditure(String expenditureCode) {
        if (receiptManager == null) {
            return null;
        }
        
        String receiptId = "RCP-" + expenditureCode;
        return receiptManager.getReceipt(receiptId);
    }
}
