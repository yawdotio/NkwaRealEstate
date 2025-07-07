package categories;

import java.util.Objects;

/**
 * Represents a category for expenditure classification.
 */
public class Category {
    private String categoryId;
    private String categoryName;
    private String description;
    private String parentCategoryId;
    private boolean isActive;
    
    public Category(String categoryId, String categoryName, String description, String parentCategoryId, boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.isActive = isActive;
    }
    
    // Getters and setters
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(String parentCategoryId) { this.parentCategoryId = parentCategoryId; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }
    
    @Override
    public String toString() {
        return String.format("Category{id='%s', name='%s', description='%s', parent='%s', active=%b}", 
                           categoryId, categoryName, description, parentCategoryId, isActive);
    }
}
