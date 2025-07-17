package categories;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages a unique set of expenditure categories using HashSet.
 */
public class CategoryManager {

    private final Set<String> categories;
    private final String filePath = "src/main/resources/categories.txt";

    public CategoryManager() {
        categories = new HashSet<>();
        loadFromFile();
    }

    /**
     * Adds a new category if it doesn't already exist.
     */
    public boolean addCategory(String category) {
        boolean added = categories.add(category.trim().toLowerCase());
        if (added) saveToFile(); // Save only if new
        return added;
    }

    /**
     * Checks if a category exists.
     */
    public boolean contains(String category) {
        return categories.contains(category.trim().toLowerCase());
    }

    /**
     * Returns all categories.
     */
    public Set<String> getAllCategories() {
        return categories;
    }

    /**
     * Load categories from file.
     */
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                categories.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Error loading categories: " + e.getMessage());
        }
    }

    /**
     * Save categories to file.
     */
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String category : categories) {
                bw.write(category);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving categories: " + e.getMessage());
        }
    }
}
