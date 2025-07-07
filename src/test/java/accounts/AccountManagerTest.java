package accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.math.BigDecimal;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for AccountManager.
 */
public class AccountManagerTest {
    
    private AccountManager accountManager;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        accountManager = new AccountManager();
    }
    
    @Test
    void testAddAccount() {
        Account account = new Account("ACC001", "Test Account", "Checking", 
                                    new BigDecimal("1000.00"), "Test Bank");
        
        accountManager.addAccount(account);
        
        Account retrieved = accountManager.getAccount("ACC001");
        assertNotNull(retrieved);
        assertEquals("Test Account", retrieved.getAccountName());
        assertEquals(new BigDecimal("1000.00"), retrieved.getBalance());
    }
    
    @Test
    void testUpdateAccount() {
        Account account = new Account("ACC001", "Test Account", "Checking", 
                                    new BigDecimal("1000.00"), "Test Bank");
        
        accountManager.addAccount(account);
        
        account.setBalance(new BigDecimal("1500.00"));
        accountManager.updateAccount(account);
        
        Account retrieved = accountManager.getAccount("ACC001");
        assertEquals(new BigDecimal("1500.00"), retrieved.getBalance());
    }
    
    @Test
    void testRemoveAccount() {
        Account account = new Account("ACC001", "Test Account", "Checking", 
                                    new BigDecimal("1000.00"), "Test Bank");
        
        accountManager.addAccount(account);
        assertTrue(accountManager.removeAccount("ACC001"));
        assertNull(accountManager.getAccount("ACC001"));
    }
    
    @Test
    void testGetAccountsBelowThreshold() {
        Account account1 = new Account("ACC001", "Low Balance", "Checking", 
                                     new BigDecimal("100.00"), "Test Bank");
        Account account2 = new Account("ACC002", "High Balance", "Savings", 
                                     new BigDecimal("5000.00"), "Test Bank");
        
        accountManager.addAccount(account1);
        accountManager.addAccount(account2);
        
        var lowBalanceAccounts = accountManager.getAccountsBelowThreshold(new BigDecimal("500.00"));
        
        assertEquals(1, lowBalanceAccounts.size());
        assertEquals("ACC001", lowBalanceAccounts.get(0).getAccountId());
    }
}
