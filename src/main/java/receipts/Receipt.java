package receipts;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a receipt or invoice in the system.
 */
public class Receipt {
    private String receiptId;
    private String receiptNumber;
    private Date receiptDate;
    private double amount;
    private String Phase;
    private String description;
    private String expenditureId;
    private ReceiptStatus status;
    private String filePath; // Path to scanned receipt image/PDF
    
    public enum ReceiptStatus {
        PENDING, VALIDATED, REJECTED, PROCESSED
    }
    
    public Receipt(String receiptId, String receiptNumber, Date receiptDate, double amount, 
                  String Phase, String description, String expenditureId, ReceiptStatus status, String filePath) {
        this.receiptId = receiptId;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.amount = amount;
        this.Phase = Phase;
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
    
    public Date getReceiptDate() { return receiptDate; }
    public void setReceiptDate(Date receiptDate) { this.receiptDate = receiptDate; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getPhase() { return Phase; }
    public void setPhase(String Phase) { this.Phase = Phase; }
    
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
        return String.format("Receipt{id='%s', number='%s', date=%s, amount=%.2f, Phase='%s', status=%s}", 
                           receiptId, receiptNumber, receiptDate, amount, Phase, status);
    }
}
