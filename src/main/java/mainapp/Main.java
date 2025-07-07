package mainapp;

import java.util.Scanner;

/**
 * Main entry point for the Construction Finance Tracker application.
 * This class provides a console-based interface for accessing all modules.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("🏗️ Construction Finance Tracker");
        System.out.println("=================================");
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getChoice(scanner);
            
            switch (choice) {
                case 1:
                    System.out.println("📊 Expenditure Management - Coming Soon!");
                    break;
                case 2:
                    System.out.println("📁 Category Management - Coming Soon!");
                    break;
                case 3:
                    System.out.println("🏦 Bank Account Management - Coming Soon!");
                    break;
                case 4:
                    System.out.println("🔍 Search & Sort - Coming Soon!");
                    break;
                case 5:
                    System.out.println("🧾 Receipt Handling - Coming Soon!");
                    break;
                case 6:
                    System.out.println("📈 Bank Tracker - Coming Soon!");
                    break;
                case 7:
                    System.out.println("📊 Financial Analysis - Coming Soon!");
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
