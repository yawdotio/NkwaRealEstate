package mainapp;

import categories.*;
import java.util.*;

/**
 * Menu handler for Category Management functionality.
 * Updated to work with the new simplified CategoryManager.
 */
public class CategoryMenu {
    private CategoryManager categoryManager;
    private Scanner scanner;
    
    public CategoryMenu(Scanner scanner) {
        this.scanner = scanner;
        this.categoryManager = new CategoryManager();
    }
    
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayCategoryMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    viewAllCategories();
                    break;
                case 3:
                    searchCategory();
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
    
    private void displayCategoryMenu() {
        System.out.println("\n--- Category Management ---");
        System.out.println("1. Add Category");
        System.out.println("2. View All Categories");
        System.out.println("3. Search Category");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addCategory() {
        System.out.println("\n--- Add New Category ---");
        
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        
        if (categoryManager.addCategory(categoryName)) {
            System.out.println("Category '" + categoryName + "' added successfully!");
        } else {
            System.out.println("Category '" + categoryName + "' already exists.");
        }
    }
    
    private void viewAllCategories() {
        System.out.println("\n--- All Categories ---");
        Set<String> categories = categoryManager.getAllCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("Available categories:");
            for (String category : categories) {
                System.out.println("  - " + category);
            }
            System.out.println("\nTotal categories: " + categories.size());
        }
    }
    
    private void searchCategory() {
        System.out.println("\n--- Search Category ---");
        System.out.print("Enter category name to search: ");
        String categoryName = scanner.nextLine();
        
        if (categoryManager.contains(categoryName)) {
            System.out.println("Category '" + categoryName + "' exists in the system.");
        } else {
            System.out.println("Category '" + categoryName + "' not found.");
        }
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
