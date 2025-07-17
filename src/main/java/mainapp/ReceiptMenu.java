package mainapp;

import receipts.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Menu handler for Receipt Management functionality.
 */
public class ReceiptMenu {
    private ReceiptManager receiptManager;
    private Scanner scanner;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public ReceiptMenu(Scanner scanner) {
        this.scanner = scanner;
        this.receiptManager = new ReceiptManager();
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayReceiptMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addReceipt();
                    break;
                case 2:
                    viewAllReceipts();
                    break;
                case 3:
                    viewReceiptsByStatus();
                    break;
                case 4:
                    viewPendingReceipts();
                    break;
                case 5:
                    processNextReceipt();
                    break;
                case 6:
                    validateReceipt();
                    break;
                case 7:
                    rejectReceipt();
                    break;
                case 8:
                    viewReceiptsByPhase();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void displayReceiptMenu() {
        System.out.println("\n--- Receipt Management ---");
        System.out.println("1. Add Receipt");
        System.out.println("2. View All Receipts");
        System.out.println("3. View Receipts by Status");
        System.out.println("4. View Pending Receipts");
        System.out.println("5. Process Next Receipt");
        System.out.println("6. Validate Receipt");
        System.out.println("7. Reject Receipt");
        System.out.println("8. View Receipts by Phase");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addReceipt() {
        System.out.println("\n--- Add New Receipt ---");
        
        System.out.print("Enter receipt ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter receipt number: ");
        String receiptNumber = scanner.nextLine();
        
        System.out.print("Enter receipt date (yyyy-MM-dd): ");
        Date date = getDateInput();
        
        System.out.print("Enter amount: ");
        double amount = getDoubleInput();
        
        System.out.print("Enter Phase: ");
        String Phase = scanner.nextLine();
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter file path (optional): ");
        String filePath = scanner.nextLine();
        
        Receipt receipt = new Receipt(id, receiptNumber, date, amount, Phase, 
                                    description, null, Receipt.ReceiptStatus.PENDING, filePath);
        receiptManager.addReceipt(receipt);
        
        System.out.println("Receipt added successfully and queued for processing!");
    }
    
    private void viewAllReceipts() {
        System.out.println("\n--- All Receipts ---");
        Collection<Receipt> receipts = receiptManager.getAllReceipts();
        
        if (receipts.isEmpty()) {
            System.out.println("No receipts found.");
        } else {
            for (Receipt receipt : receipts) {
                System.out.println(receipt);
            }
        }
    }
    
    private void viewReceiptsByStatus() {
        System.out.println("Select status:");
        System.out.println("1. PENDING");
        System.out.println("2. VALIDATED");
        System.out.println("3. REJECTED");
        System.out.println("4. PROCESSED");
        System.out.print("Enter choice: ");
        
        int statusChoice = getChoice();
        Receipt.ReceiptStatus status;
        
        switch (statusChoice) {
            case 1: status = Receipt.ReceiptStatus.PENDING; break;
            case 2: status = Receipt.ReceiptStatus.VALIDATED; break;
            case 3: status = Receipt.ReceiptStatus.REJECTED; break;
            case 4: status = Receipt.ReceiptStatus.PROCESSED; break;
            default:
                System.out.println("Invalid status choice.");
                return;
        }
        
        List<Receipt> receipts = receiptManager.getReceiptsByStatus(status);
        
        if (receipts.isEmpty()) {
            System.out.println("No receipts found with status: " + status);
        } else {
            System.out.println("\n--- Receipts with Status: " + status + " ---");
            for (Receipt receipt : receipts) {
                System.out.println(receipt);
            }
        }
    }
    
    private void viewPendingReceipts() {
        System.out.println("\n--- Pending Receipts (Processing Queue) ---");
        List<Receipt> pendingReceipts = receiptManager.getPendingReceipts();
        
        if (pendingReceipts.isEmpty()) {
            System.out.println("No pending receipts in queue.");
        } else {
            System.out.println("Queue size: " + receiptManager.getProcessingQueueSize());
            for (Receipt receipt : pendingReceipts) {
                System.out.println(receipt);
            }
        }
    }
    
    private void processNextReceipt() {
        Receipt receipt = receiptManager.getNextReceiptToProcess();
        
        if (receipt == null) {
            System.out.println("No receipts in processing queue.");
            return;
        }
        
        System.out.println("Processing receipt: " + receipt);
        System.out.println("What action would you like to take?");
        System.out.println("1. Mark as VALIDATED");
        System.out.println("2. Mark as REJECTED");
        System.out.println("3. Mark as PROCESSED");
        System.out.print("Enter choice: ");
        
        int action = getChoice();
        Receipt.ReceiptStatus newStatus;
        
        switch (action) {
            case 1: newStatus = Receipt.ReceiptStatus.VALIDATED; break;
            case 2: newStatus = Receipt.ReceiptStatus.REJECTED; break;
            case 3: newStatus = Receipt.ReceiptStatus.PROCESSED; break;
            default:
                System.out.println("Invalid choice. Receipt returned to queue.");
                return;
        }
        
        receiptManager.processReceipt(receipt.getReceiptId(), newStatus);
        System.out.println("Receipt processed with status: " + newStatus);
    }
    
    private void validateReceipt() {
        System.out.print("Enter receipt ID to validate: ");
        String receiptId = scanner.nextLine();
        
        System.out.print("Enter expenditure ID to link: ");
        String expenditureId = scanner.nextLine();
        
        if (receiptManager.validateReceipt(receiptId, expenditureId)) {
            System.out.println("Receipt validated and linked to expenditure!");
        } else {
            System.out.println("Receipt not found.");
        }
    }
    
    private void rejectReceipt() {
        System.out.print("Enter receipt ID to reject: ");
        String receiptId = scanner.nextLine();
        
        receiptManager.rejectReceipt(receiptId);
        System.out.println("Receipt rejected.");
    }
    
    private void viewReceiptsByPhase() {
        System.out.print("Enter Phase name: ");
        String Phase = scanner.nextLine();
        
        List<Receipt> receipts = receiptManager.getReceiptsByPhase(Phase);
        
        if (receipts.isEmpty()) {
            System.out.println("No receipts found for Phase: " + Phase);
        } else {
            System.out.println("\n--- Receipts for Phase: " + Phase + " ---");
            for (Receipt receipt : receipts) {
                System.out.println(receipt);
            }
        }
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Please enter a valid number: ");
            }
        }
    }
    
    private Date getDateInput() {
        while (true) {
            try {
                return DATE_FORMAT.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }
}
