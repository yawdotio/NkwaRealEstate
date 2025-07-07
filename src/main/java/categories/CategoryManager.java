package categories;

import java.io.*;
import java.util.*;

/**
 * Manages categories using HashSet for fast lookup and uniqueness.
 * Handles category creation, retrieval, and persistence.
 */
public class CategoryManager {
    private Set<Category> categories;
    private Map<String, Category> categoryIndex; // For fast lookup by ID
    private static final String CATEGORIES_FILE = "src/main/resources/categories.txt";
    
    public CategoryManager() {
        this.categories = new HashSet<>();
        this.categoryIndex = new HashMap<>();
        loadCategories();
    }
    
    /**
     * Adds a new category to the system.
     */
    public boolean addCategory(Category category) {
        if (categories.add(category)) {
            categoryIndex.put(category.getCategoryId(), category);
            saveCategories();
            return true;
        }
        return false; // Category already exists
    }
    
    /**
     * Retrieves a category by ID.
     */
    public Category getCategory(String categoryId) {
        return categoryIndex.get(categoryId);
    }
    
    /**
     * Returns all categories.
     */
    public Set<Category> getAllCategories() {
        return new HashSet<>(categories);
    }
    
    /**
     * Returns all active categories.
     */
    public Set<Category> getActiveCategories() {
        Set<Category> activeCategories = new HashSet<>();
        for (Category category : categories) {
            if (category.isActive()) {
                activeCategories.add(category);
            }
        }
        return activeCategories;
    }
    
    /**
     * Updates an existing category.
     */
    public void updateCategory(Category category) {
        if (categories.contains(category)) {
            categories.remove(category);
            categories.add(category);
            categoryIndex.put(category.getCategoryId(), category);
            saveCategories();
        }
    }
    
    /**
     * Removes a category by ID.
     */
    public boolean removeCategory(String categoryId) {
        Category category = categoryIndex.get(categoryId);
        if (category != null) {
            categories.remove(category);
            categoryIndex.remove(categoryId);
            saveCategories();
            return true;
        }
        return false;
    }
    
    /**
     * Deactivates a category instead of removing it.
     */
    public boolean deactivateCategory(String categoryId) {
        Category category = categoryIndex.get(categoryId);
        if (category != null) {
            category.setActive(false);
            saveCategories();
            return true;
        }
        return false;
    }
    
    /**
     * Gets subcategories of a parent category.
     */
    public Set<Category> getSubcategories(String parentCategoryId) {
        Set<Category> subcategories = new HashSet<>();
        for (Category category : categories) {
            if (parentCategoryId.equals(category.getParentCategoryId())) {
                subcategories.add(category);
            }
        }
        return subcategories;
    }
    
    /**
     * Gets root categories (categories without parent).
     */
    public Set<Category> getRootCategories() {
        Set<Category> rootCategories = new HashSet<>();
        for (Category category : categories) {
            if (category.getParentCategoryId() == null || category.getParentCategoryId().isEmpty()) {
                rootCategories.add(category);
            }
        }
        return rootCategories;
    }
    
    /**
     * Checks if a category exists.
     */
    public boolean categoryExists(String categoryId) {
        return categoryIndex.containsKey(categoryId);
    }
    
    /**
     * Searches categories by name (case-insensitive).
     */
    public Set<Category> searchCategoriesByName(String name) {
        Set<Category> result = new HashSet<>();
        String searchTerm = name.toLowerCase();
        for (Category category : categories) {
            if (category.getCategoryName().toLowerCase().contains(searchTerm)) {
                result.add(category);
            }
        }
        return result;
    }
    
    /**
     * Loads categories from file.
     */
    private void loadCategories() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Category category = parseCategory(line);
                    if (category != null) {
                        categories.add(category);
                        categoryIndex.put(category.getCategoryId(), category);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }
    
    /**
     * Saves categories to file.
     */
    private void saveCategories() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATEGORIES_FILE))) {
            for (Category category : categories) {
                writer.println(formatCategory(category));
            }
        } catch (IOException e) {
            System.err.println("Error saving categories: " + e.getMessage());
        }
    }
    
    /**
     * Parses a line from the file into a Category object.
     * Format: categoryId,categoryName,description,parentCategoryId,isActive
     */
    private Category parseCategory(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                String categoryId = parts[0].trim();
                String categoryName = parts[1].trim();
                String description = parts[2].trim();
                String parentCategoryId = parts[3].trim();
                boolean isActive = Boolean.parseBoolean(parts[4].trim());
                
                // Handle empty parent category
                if (parentCategoryId.isEmpty()) {
                    parentCategoryId = null;
                }
                
                return new Category(categoryId, categoryName, description, parentCategoryId, isActive);
            }
        } catch (Exception e) {
            System.err.println("Error parsing category line: " + line);
        }
        return null;
    }
    
    /**
     * Formats a Category object for file storage.
     */
    private String formatCategory(Category category) {
        String parentId = category.getParentCategoryId() != null ? category.getParentCategoryId() : "";
        return String.format("%s,%s,%s,%s,%b",
                           category.getCategoryId(),
                           category.getCategoryName(),
                           category.getDescription(),
                           parentId,
                           category.isActive());
    }
}
