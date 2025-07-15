package mainapp;

import categories.*;
import java.util.*;

/**
 * Menu handler for Category Management functionality.
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
                    viewActiveCategories();
                    break;
                case 4:
                    viewSubcategories();
                    break;
                case 5:
                    searchCategories();
                    break;
                case 6:
                    updateCategory();
                    break;
                case 7:
                    deactivateCategory();
                    break;
                case 8:
                    deleteCategory();
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
        System.out.println("3. View Active Categories");
        System.out.println("4. View Subcategories");
        System.out.println("5. Search Categories");
        System.out.println("6. Update Category");
        System.out.println("7. Deactivate Category");
        System.out.println("8. Delete Category");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    
    private void addCategory() {
        System.out.println("\n--- Add New Category ---");
        
        System.out.print("Enter category ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter parent category ID (or press Enter for root category): ");
        String parentId = scanner.nextLine();
        if (parentId.trim().isEmpty()) {
            parentId = null;
        }
        
        Category category = new Category(id, name, description, parentId, true);
        
        if (categoryManager.addCategory(category)) {
            System.out.println("Category added successfully!");
        } else {
            System.out.println("Category with this ID already exists.");
        }
    }
    
    private void viewAllCategories() {
        System.out.println("\n--- All Categories ---");
        Set<Category> categories = categoryManager.getAllCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            for (Category category : categories) {
                System.out.println(category);
            }
        }
    }
    
    private void viewActiveCategories() {
        System.out.println("\n--- Active Categories ---");
        Set<Category> categories = categoryManager.getActiveCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No active categories found.");
        } else {
            for (Category category : categories) {
                System.out.println(category);
            }
        }
    }
    
    private void viewSubcategories() {
        System.out.print("Enter parent category ID: ");
        String parentId = scanner.nextLine();
        
        Set<Category> subcategories = categoryManager.getSubcategories(parentId);
        
        if (subcategories.isEmpty()) {
            System.out.println("No subcategories found for parent: " + parentId);
        } else {
            System.out.println("\n--- Subcategories of " + parentId + " ---");
            for (Category category : subcategories) {
                System.out.println(category);
            }
        }
    }
    
    private void searchCategories() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        
        Set<Category> categories = categoryManager.searchCategoriesByName(searchTerm);
        
        if (categories.isEmpty()) {
            System.out.println("No categories found matching: " + searchTerm);
        } else {
            System.out.println("\n--- Search Results ---");
            for (Category category : categories) {
                System.out.println(category);
            }
        }
    }
    
    private void updateCategory() {
        System.out.print("Enter category ID to update: ");
        String id = scanner.nextLine();
        
        Category category = categoryManager.getCategory(id);
        if (category == null) {
            System.out.println("Category not found.");
            return;
        }
        
        System.out.println("Current category: " + category);
        System.out.println("Enter new values (press Enter to keep current value):");
        
        System.out.print("Name [" + category.getCategoryName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            category.setCategoryName(name);
        }
        
        System.out.print("Description [" + category.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            category.setDescription(description);
        }
        
        categoryManager.updateCategory(category);
        System.out.println("Category updated successfully!");
    }
    
    private void deactivateCategory() {
        System.out.print("Enter category ID to deactivate: ");
        String id = scanner.nextLine();
        
        if (categoryManager.deactivateCategory(id)) {
            System.out.println("Category deactivated successfully!");
        } else {
            System.out.println("Category not found.");
        }
    }
    
    private void deleteCategory() {
        System.out.print("Enter category ID to delete: ");
        String id = scanner.nextLine();
        
        System.out.print("Are you sure? This action cannot be undone (y/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            if (categoryManager.removeCategory(id)) {
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Category not found.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
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
