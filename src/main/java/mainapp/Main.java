package mainapp;

import java.util.Scanner;

/**
 * Main entry point for the Construction Finance Tracker application.
 * This class provides a console-based interface for accessing all modules.
 */
public class Main {
    private static ExpenditureMenu expenditureMenu;
    private static CategoryMenu categoryMenu;
    private static AccountMenu accountMenu;
    private static SearchSortMenu searchSortMenu;
    private static ReceiptMenu receiptMenu;
    private static BankTrackerMenu bankTrackerMenu;
    private static FinancialAnalysisMenu financialAnalysisMenu;
    
    public static void main(String[] args) {
        System.out.println("🏗️ Construction Finance Tracker");
        System.out.println("=================================");
        
        Scanner scanner = new Scanner(System.in);
        
        // Initialize menu handlers
        expenditureMenu = new ExpenditureMenu(scanner);
        categoryMenu = new CategoryMenu(scanner);
        accountMenu = new AccountMenu(scanner);
        searchSortMenu = new SearchSortMenu(scanner);
        receiptMenu = new ReceiptMenu(scanner);
        bankTrackerMenu = new BankTrackerMenu(scanner);
        financialAnalysisMenu = new FinancialAnalysisMenu(scanner);
        
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getChoice(scanner);
            
            switch (choice) {
                case 1:
                    expenditureMenu.showMenu();
                    break;
                case 2:
                    categoryMenu.showMenu();
                    break;
                case 3:
                    accountMenu.showMenu();
                    break;
                case 4:
                    searchSortMenu.showMenu();
                    break;
                case 5:
                    receiptMenu.showMenu();
                    break;
                case 6:
                    bankTrackerMenu.showMenu();
                    break;
                case 7:
                    financialAnalysisMenu.showMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Construction Finance Tracker!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Expenditure Management");
        System.out.println("2. Category Management");
        System.out.println("3. Bank Account Management");
        System.out.println("4. Search & Sort");
        System.out.println("5. Receipt Handling");
        System.out.println("6. Bank Tracker");
        System.out.println("7. Financial Analysis");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
