package receipts;

import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Manages receipts using Queue for processing workflow.
 * Handles receipt validation, processing, and persistence.
 */
public class ReceiptManager {
    private Map<String, Receipt> receipts;
    private Queue<Receipt> processingQueue;
    private Stack<Receipt> recentlyProcessed;
    private static final String RECEIPTS_FILE = "src/main/resources/receipts.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final int MAX_RECENT_PROCESSED = 50;
    
    public ReceiptManager() {
        this.receipts = new HashMap<>();
        this.processingQueue = new LinkedList<>();
        this.recentlyProcessed = new Stack<>();
        loadReceipts();
    }
    
    /**
     * Adds a new receipt to the system and queues it for processing.
     */
    public void addReceipt(Receipt receipt) {
        receipts.put(receipt.getReceiptId(), receipt);
        if (receipt.getStatus() == Receipt.ReceiptStatus.PENDING) {
            processingQueue.offer(receipt);
        }
        saveReceipts();
    }
    
    /**
     * Retrieves a receipt by ID.
     */
    public Receipt getReceipt(String receiptId) {
        return receipts.get(receiptId);
    }
    
    /**
     * Returns all receipts.
     */
    public Collection<Receipt> getAllReceipts() {
        return receipts.values();
    }
    
    /**
     * Gets the next receipt from the processing queue.
     */
    public Receipt getNextReceiptToProcess() {
        return processingQueue.poll();
    }
    
    /**
     * Processes a receipt and updates its status.
     */
    public void processReceipt(String receiptId, Receipt.ReceiptStatus newStatus) {
        Receipt receipt = receipts.get(receiptId);
        if (receipt != null) {
            receipt.setStatus(newStatus);
            
            // Remove from processing queue if it's there
            processingQueue.remove(receipt);
            
            // Add to recently processed stack
            recentlyProcessed.push(receipt);
            
            // Maintain stack size limit
            if (recentlyProcessed.size() > MAX_RECENT_PROCESSED) {
                recentlyProcessed.removeElementAt(0);
            }
            
            saveReceipts();
        }
    }
    
    /**
     * Validates a receipt against an expenditure.
     */
    public boolean validateReceipt(String receiptId, String expenditureId) {
        Receipt receipt = receipts.get(receiptId);
        if (receipt != null) {
            receipt.setExpenditureId(expenditureId);
            receipt.setStatus(Receipt.ReceiptStatus.VALIDATED);
            processingQueue.remove(receipt);
            recentlyProcessed.push(receipt);
            saveReceipts();
            return true;
        }
        return false;
    }
    
    /**
     * Rejects a receipt with a reason.
     */
    public void rejectReceipt(String receiptId) {
        Receipt receipt = receipts.get(receiptId);
        if (receipt != null) {
            receipt.setStatus(Receipt.ReceiptStatus.REJECTED);
            processingQueue.remove(receipt);
            recentlyProcessed.push(receipt);
            saveReceipts();
        }
    }
    
    /**
     * Gets receipts by status.
     */
    public List<Receipt> getReceiptsByStatus(Receipt.ReceiptStatus status) {
        List<Receipt> result = new ArrayList<>();
        for (Receipt receipt : receipts.values()) {
            if (receipt.getStatus() == status) {
                result.add(receipt);
            }
        }
        return result;
    }
    
    /**
     * Gets receipts by Phase.
     */
    public List<Receipt> getReceiptsByPhase(String Phase) {
        List<Receipt> result = new ArrayList<>();
        for (Receipt receipt : receipts.values()) {
            if (Phase.equalsIgnoreCase(receipt.getPhase())) {
                result.add(receipt);
            }
        }
        return result;
    }
    
    /**
     * Gets the size of the processing queue.
     */
    public int getProcessingQueueSize() {
        return processingQueue.size();
    }
    
    /**
     * Gets recently processed receipts.
     */
    public List<Receipt> getRecentlyProcessed() {
        return new ArrayList<>(recentlyProcessed);
    }
    
    /**
     * Gets pending receipts (in queue).
     */
    public List<Receipt> getPendingReceipts() {
        return new ArrayList<>(processingQueue);
    }
    
    /**
     * Loads receipts from file.
     */
    private void loadReceipts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RECEIPTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Receipt receipt = parseReceipt(line);
                    if (receipt != null) {
                        receipts.put(receipt.getReceiptId(), receipt);
                        if (receipt.getStatus() == Receipt.ReceiptStatus.PENDING) {
                            processingQueue.offer(receipt);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading receipts: " + e.getMessage());
        }
    }
    
    /**
     * Saves receipts to file.
     */
    private void saveReceipts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RECEIPTS_FILE))) {
            for (Receipt receipt : receipts.values()) {
                writer.println(formatReceipt(receipt));
            }
        } catch (IOException e) {
            System.err.println("Error saving receipts: " + e.getMessage());
        }
    }
    
    /**
     * Parses a line from the file into a Receipt object.
     * Format: receiptId,receiptNumber,receiptDate,amount,Phase,description,expenditureId,status,filePath
     */
    private Receipt parseReceipt(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 9) {
                String receiptId = parts[0].trim();
                String receiptNumber = parts[1].trim();
                Date receiptDate = DATE_FORMAT.parse(parts[2].trim());
                double amount = Double.parseDouble(parts[3].trim());
                String Phase = parts[4].trim();
                String description = parts[5].trim();
                String expenditureId = parts[6].trim();
                Receipt.ReceiptStatus status = Receipt.ReceiptStatus.valueOf(parts[7].trim());
                String filePath = parts[8].trim();
                
                // Handle empty expenditure ID
                if (expenditureId.isEmpty()) {
                    expenditureId = null;
                }
                
                return new Receipt(receiptId, receiptNumber, receiptDate, amount, Phase, description, expenditureId, status, filePath);
            }
        } catch (Exception e) {
            System.err.println("Error parsing receipt line: " + line);
        }
        return null;
    }
    
    /**
     * Formats a Receipt object for file storage.
     */
    private String formatReceipt(Receipt receipt) {
        String expenditureId = receipt.getExpenditureId() != null ? receipt.getExpenditureId() : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                           receipt.getReceiptId(),
                           receipt.getReceiptNumber(),
                           DATE_FORMAT.format(receipt.getReceiptDate()),
                           String.valueOf(receipt.getAmount()),
                           receipt.getPhase(),
                           receipt.getDescription(),
                           expenditureId,
                           receipt.getStatus().name(),
                           receipt.getFilePath());
    }
}
