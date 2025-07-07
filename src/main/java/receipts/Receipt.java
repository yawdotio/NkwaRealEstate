package receipts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a receipt or invoice in the system.
 */
public class Receipt {
    private String receiptId;
    private String receiptNumber;
    private LocalDate receiptDate;
    private BigDecimal amount;
    private String vendor;
    private String description;
    private String expenditureId;
    private ReceiptStatus status;
    private String filePath; // Path to scanned receipt image/PDF
    
    public enum ReceiptStatus {
        PENDING, VALIDATED, REJECTED, PROCESSED
    }
    
    public Receipt(String receiptId, String receiptNumber, LocalDate receiptDate, BigDecimal amount, 
                  String vendor, String description, String expenditureId, ReceiptStatus status, String filePath) {
        this.receiptId = receiptId;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.vendor = vendor;
        this.description = description;
        this.expenditureId = expenditureId;
        this.status = status;
        this.filePath = filePath;
    }
    
    // Getters and setters
    public String getReceiptId() { return receiptId; }
    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }
    
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    
    public LocalDate getReceiptDate() { return receiptDate; }
    public void setReceiptDate(LocalDate receiptDate) { this.receiptDate = receiptDate; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getExpenditureId() { return expenditureId; }
    public void setExpenditureId(String expenditureId) { this.expenditureId = expenditureId; }
    
    public ReceiptStatus getStatus() { return status; }
    public void setStatus(ReceiptStatus status) { this.status = status; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(receiptId, receipt.receiptId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(receiptId);
    }
    
    @Override
    public String toString() {
        return String.format("Receipt{id='%s', number='%s', date=%s, amount=%s, vendor='%s', status=%s}", 
                           receiptId, receiptNumber, receiptDate, amount, vendor, status);
    }
}
