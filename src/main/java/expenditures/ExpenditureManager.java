package expenditures;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages expenditure records using HashMap and LinkedList.
 * Handles expenditure creation, retrieval, and persistence.
 */
public class ExpenditureManager {
    private Map<String, Expenditure> expenditures;
    private LinkedList<Expenditure> expenditureHistory;
    private static final String EXPENDITURES_FILE = "src/main/resources/expenditures.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ExpenditureManager() {
        this.expenditures = new HashMap<>();
        this.expenditureHistory = new LinkedList<>();
        loadExpenditures();
    }
    
    /**
     * Adds a new expenditure to the system.
     */
    public void addExpenditure(Expenditure expenditure) {
        expenditures.put(expenditure.getExpenditureId(), expenditure);
        expenditureHistory.addFirst(expenditure); // Most recent first
        saveExpenditures();
    }
    
    /**
     * Retrieves an expenditure by ID.
     */
    public Expenditure getExpenditure(String expenditureId) {
        return expenditures.get(expenditureId);
    }
    
    /**
     * Returns all expenditures.
     */
    public Collection<Expenditure> getAllExpenditures() {
        return expenditures.values();
    }
    
    /**
     * Returns expenditure history (most recent first).
     */
    public List<Expenditure> getExpenditureHistory() {
        return new ArrayList<>(expenditureHistory);
    }
    
    /**
     * Updates an existing expenditure.
     */
    public void updateExpenditure(Expenditure expenditure) {
        if (expenditures.containsKey(expenditure.getExpenditureId())) {
            expenditures.put(expenditure.getExpenditureId(), expenditure);
            // Update in history list
            expenditureHistory.remove(expenditure);
            expenditureHistory.addFirst(expenditure);
            saveExpenditures();
        }
    }
    
    /**
     * Removes an expenditure by ID.
     */
    public boolean removeExpenditure(String expenditureId) {
        Expenditure removed = expenditures.remove(expenditureId);
        if (removed != null) {
            expenditureHistory.remove(removed);
            saveExpenditures();
            return true;
        }
        return false;
    }
    
    /**
     * Gets expenditures by account ID.
     */
    public List<Expenditure> getExpendituresByAccount(String accountId) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (accountId.equals(exp.getAccountId())) {
                result.add(exp);
            }
        }
        return result;
    }
    
    /**
     * Gets expenditures by category ID.
     */
    public List<Expenditure> getExpendituresByCategory(String categoryId) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (categoryId.equals(exp.getCategoryId())) {
                result.add(exp);
            }
        }
        return result;
    }
    
    /**
     * Gets expenditures within a date range.
     */
    public List<Expenditure> getExpendituresByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            LocalDate expDate = exp.getDate();
            if (!expDate.isBefore(startDate) && !expDate.isAfter(endDate)) {
                result.add(exp);
            }
        }
        return result;
    }
    
    /**
     * Calculates total expenditures for a given account.
     */
    public BigDecimal getTotalExpendituresByAccount(String accountId) {
        BigDecimal total = BigDecimal.ZERO;
        for (Expenditure exp : expenditures.values()) {
            if (accountId.equals(exp.getAccountId())) {
                total = total.add(exp.getAmount());
            }
        }
        return total;
    }
    
    /**
     * Loads expenditures from file.
     */
    private void loadExpenditures() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPENDITURES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Expenditure expenditure = parseExpenditure(line);
                    if (expenditure != null) {
                        expenditures.put(expenditure.getExpenditureId(), expenditure);
                        expenditureHistory.addLast(expenditure);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading expenditures: " + e.getMessage());
        }
    }
    
    /**
     * Saves expenditures to file.
     */
    private void saveExpenditures() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENDITURES_FILE))) {
            for (Expenditure expenditure : expenditures.values()) {
                writer.println(formatExpenditure(expenditure));
            }
        } catch (IOException e) {
            System.err.println("Error saving expenditures: " + e.getMessage());
        }
    }
    
    /**
     * Parses a line from the file into an Expenditure object.
     * Format: expenditureId,description,amount,date,accountId,categoryId,vendor,projectId
     */
    private Expenditure parseExpenditure(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 8) {
                String expenditureId = parts[0].trim();
                String description = parts[1].trim();
                BigDecimal amount = new BigDecimal(parts[2].trim());
                LocalDate date = LocalDate.parse(parts[3].trim(), DATE_FORMAT);
                String accountId = parts[4].trim();
                String categoryId = parts[5].trim();
                String vendor = parts[6].trim();
                String projectId = parts[7].trim();
                
                return new Expenditure(expenditureId, description, amount, date, accountId, categoryId, vendor, projectId);
            }
        } catch (Exception e) {
            System.err.println("Error parsing expenditure line: " + line);
        }
        return null;
    }
    
    /**
     * Formats an Expenditure object for file storage.
     */
    private String formatExpenditure(Expenditure expenditure) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                           expenditure.getExpenditureId(),
                           expenditure.getDescription(),
                           expenditure.getAmount().toString(),
                           expenditure.getDate().format(DATE_FORMAT),
                           expenditure.getAccountId(),
                           expenditure.getCategoryId(),
                           expenditure.getVendor(),
                           expenditure.getProjectId());
    }
}
