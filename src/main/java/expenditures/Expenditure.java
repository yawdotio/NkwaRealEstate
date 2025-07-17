package expenditures;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Represents a single expenditure record made by the company.
 * It contains key details such as amount, phase, category, and the bank account used.
 */
public class Expenditure {
    private String code;          // Unique code to identify expenditure
    private double amount;        // Amount spent
    private Date date;            // Date of the transaction
    private String phase;         // Phase: construction, marketing, sales, etc.
    private String category;      // E.g., Cement, Printing, Advertising
    private String accountId;     // ID of the bank account used
    private String receiptPath;   // Optional path to receipt/invoice file
    
    // Constructor to initialize expenditure
    public Expenditure(String code, double amount, Date date, String phase,
                       String category, String accountId, String receiptPath) {
        this.code = code;
        this.amount = amount;
        this.date = date;
        this.phase = phase;
        this.category = category;
        this.accountId = accountId;
        this.receiptPath = receiptPath;
    }
    
    // Getters
    public String getCode() { return code; }
    public double getAmount() { return amount; }
    public Date getDate() { return date; }
    public String getPhase() { return phase; }
    public String getCategory() { return category; }
    public String getAccountId() { return accountId; }
    public String getReceiptPath() { return receiptPath; }

    // Setters
    public void setReceiptPath(String receiptPath) {
        this.receiptPath = receiptPath;
    }

    // Format the date nicely for reports or display
    public String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expenditure that = (Expenditure) o;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    // Convert object to readable string format
    @Override
    public String toString() {
        return "Expenditure [" +
               "Code: " + code +
               ", Amount: GHS " + amount +
               ", Date: " + getFormattedDate() +
               ", Phase: " + phase +
               ", Category: " + category +
               ", Account ID: " + accountId +
               "]";
    }
}
