# 🏗️ Nkwa Real Estate Finance Tracker

A modular Java project for tracking and analyzing expenditures in construction and media-related projects. Built for team collaboration with clear separation of concerns and data structures.

---

## 📁 Project Structure

Each module is maintained in its own package:

```
src/main/java/
├── accounts/       # Bank Account Ledger
├── analysis/       # Financial Analysis
├── categories/     # Category Management
├── expenditures/   # Expenditure Records
├── receipts/       # Invoice/Receipt Handling
├── searchsort/     # Search & Sort Utilities
├── tracker/        # Bank Tracker (Min Heap)
└── mainapp/        # Main entry point (Main.java)
```

---

## 🚀 Getting Started

### 1. Prerequisites

Ensure you have the following installed:

- [Java JDK 17+](https://adoptium.net/)
- [Apache Maven](https://maven.apache.org/install.html)
- Git
- IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [VS Code](https://code.visualstudio.com/)

Verify:
```bash
java -version
mvn -version
```

---

### 2. Clone the Project

```bash
git clone https://github.com/yourusername/ConstructionFinanceTracker.git
cd ConstructionFinanceTracker
```

---

### 3. Build the Project

```bash
mvn clean install
```

This command compiles the code and runs all tests.

---

### 4. Run the Application

If you have a `Main.java` class in `mainapp/` with a proper main method:

```bash
mvn exec:java -Dexec.mainClass="mainapp.Main"
```

> ⚠️ To use `exec:java`, make sure this plugin is in your `pom.xml`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.1.0</version>
    </plugin>
  </plugins>
</build>
```

---

## 🧑‍💻 Contributing Guidelines

### Project Modules & Responsibilities

| Module              | Package         | Structure Used           | Task Summary                                |
| ------------------- | --------------- | ------------------------ | ------------------------------------------- |
| Expenditures        | `expenditures/` | `HashMap` / `LinkedList` | Add/view expenses, link to account/category |
| Category Management | `categories/`   | `HashSet`                | Manage and store categories                 |
| Bank Accounts       | `accounts/`     | `Map`, optional `Graph`  | Track accounts and balances                 |
| Search & Sort       | `searchsort/`   | Arrays, Binary Search    | Search, filter, and sort records            |
| Invoice Handling    | `receipts/`     | `Queue` or `Stack`       | Simulate receipt validation                 |
| Bank Tracker        | `tracker/`      | `Min Heap`               | Track lowest balances, raise alerts         |
| Financial Analysis  | `analysis/`     | Arrays / Maps            | Forecasting, burn rate analysis             |

Each contributor should:

* Work only inside their designated package.
* Create a `Manager.java` or `Utils.java` class if needed.
* Read and write data to the appropriate `.txt` file in `/resources/`.

---

## 📂 Data Files

All persistent data is stored in:

```
src/main/resources/
├── expenditures.txt
├── categories.txt
├── accounts.txt
└── receipts.txt
```

Each file follows a line-based format. Define your own serialization style in your module (e.g., CSV or custom delimiter).

---

## 🧪 Running Tests

Add your tests in:

```
src/test/java/<your_package>/
```

Run all tests using:

```bash
mvn test
```

---

## 📦 Packaging (Optional)

To create a `.jar` file:

```bash
mvn package
```

This will generate:

```
target/ConstructionFinanceTracker-1.0-SNAPSHOT.jar
```

---

## 📝 License

This project is open-source. Add your license in the `LICENSE` file.

---

## 🙌 Acknowledgments

Thanks to all collaborators working on this modular finance tracker!