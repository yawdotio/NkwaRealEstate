package expenditures;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Manages a collection of Expenditure records using a HashMap.
 * Handles loading from and saving to a text file.
 */
public class ExpenditureManager {

    private final HashMap<String, Expenditure> expenditures; // Key: code
    private final String filePath = "src/main/resources/expenditures.txt";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ExpenditureManager() {
        expenditures = new HashMap<>();
        loadFromFile(); // Load data on startup
    }

    /**
     * Adds a new expenditure to the system and saves it to file.
     */
    public void addExpenditure(Expenditure exp) {
        expenditures.put(exp.getCode(), exp);
        saveToFile();
    }

    /**
     * Finds an expenditure by its code.
     */
    public Expenditure findByCode(String code) {
        return expenditures.get(code);
    }

    /**
     * Returns a list of all expenditures.
     */
    public List<Expenditure> getAll() {
        return new ArrayList<>(expenditures.values());
    }

    /**
     * Loads expenditure data from a text file into memory.
     * Format: code|amount|date|phase|category|accountId|receiptPath
     */
    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    String code = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    Date date = dateFormat.parse(parts[2]);
                    String phase = parts[3];
                    String category = parts[4];
                    String accountId = parts[5];
                    String receiptPath = parts[6];

                    Expenditure exp = new Expenditure(code, amount, date, phase, category, accountId, receiptPath);
                    expenditures.put(code, exp);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading expenditures: " + e.getMessage());
        }
    }

    /**
     * Saves all expenditure records to a text file.
     */
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Expenditure exp : expenditures.values()) {
                String line = exp.getCode() + "|" +
                              exp.getAmount() + "|" +
                              dateFormat.format(exp.getDate()) + "|" +
                              exp.getPhase() + "|" +
                              exp.getCategory() + "|" +
                              exp.getAccountId() + "|" +
                              (exp.getReceiptPath() == null ? "" : exp.getReceiptPath());

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving expenditures: " + e.getMessage());
        }
    }

    public List<Expenditure> searchByDateRange(Date start, Date end) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (exp.getDate().after(start) && exp.getDate().before(end)) {
                result.add(exp);
            }
        }
        return result;
    }

    public List<Expenditure> searchByPhase(String phase) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (phase.equals(exp.getPhase())) {
                result.add(exp);
            }
        }
        return result;
    }

    public List<Expenditure> searchByCategory(String category) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (category.equals(exp.getCategory())) {
                result.add(exp);
            }
        }
        return result;
    }

    public List<Expenditure> searchByAccountId(String accountId) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure exp : expenditures.values()) {
            if (accountId.equals(exp.getAccountId())) {
                result.add(exp);
            }
        }
        return result;
    }

    public double getTotalSpentByAccount(String accountId) {
        double total = 0.0;
        for (Expenditure exp : expenditures.values()) {
            if (accountId.equals(exp.getAccountId())) {
                total += exp.getAmount();
            }
        }
        return total;
    }

    public double getTotalSpentByPhase(String phase) {
        double total = 0.0;
        for (Expenditure exp : expenditures.values()) {
            if (phase.equals(exp.getPhase())) {
                total += exp.getAmount();
            }
        }
        return total;
    }

    public double getTotalSpentByCategory(String category) {
        double total = 0.0;
        for (Expenditure exp : expenditures.values()) {
            if (category.equals(exp.getCategory())) {
                total += exp.getAmount();
            }
        }
        return total;
    }
    
    // Backward compatibility methods for existing menus
    public java.util.Collection<Expenditure> getAllExpenditures() {
        return getAll();
    }
    
    public Expenditure getExpenditure(String code) {
        return findByCode(code);
    }
    
    public List<Expenditure> getExpendituresByAccount(String accountId) {
        return searchByAccountId(accountId);
    }
    
    public List<Expenditure> getExpendituresByCategory(String category) {
        return searchByCategory(category);
    }
    
    public boolean removeExpenditure(String code) {
        Expenditure removed = expenditures.remove(code);
        if (removed != null) {
            saveToFile();
            return true;
        }
        return false;
    }
    
    public void updateExpenditure(Expenditure expenditure) {
        expenditures.put(expenditure.getCode(), expenditure);
        saveToFile();
    }
}
