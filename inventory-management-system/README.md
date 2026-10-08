# Inventory Management System

A modern desktop **Inventory Management System** built with **Java 21, JavaFX, Maven, and SQLite**. The application provides a clean dashboard for managing products, vendors, stock movements, and inventory health.

## Project Overview

This project demonstrates a complete Java desktop application with layered architecture, persistent SQLite storage, validation, transactional stock operations, automated tests, and a polished JavaFX dashboard.

## Features

- Dashboard with total items, units, low-stock count, and vendor count
- Item CRUD: add, edit, delete, search
- Vendor CRUD: add, edit, delete, search
- Stock In and Stock Out workflows
- Negative-stock prevention
- Low-stock highlighting (quantity <= 5)
- SQLite persistence with JDBC
- Transaction-safe stock adjustments
- Input validation and confirmation dialogs
- JUnit 5 automated tests
- GitHub Actions CI
- Java 21 / Maven build

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application language/runtime |
| JavaFX 21 | Desktop user interface |
| Maven | Build and dependency management |
| SQLite | Local persistent database |
| JDBC | Database connectivity |
| JUnit 5 | Automated testing |
| GitHub Actions | Continuous integration |
| CSS | JavaFX UI styling |

## Architecture

```
JavaFX UI
   ↓
Inventory Service
   ↓
DAO / JDBC
   ↓
SQLite Database
```

## Project Structure

```
inventory-management-system/
├── pom.xml
├── README.md
├── src/main/java/com/example/inventory/
│   ├── MainApp.java
│   ├── dao/
│   │   ├── Database.java
│   │   ├── ItemDao.java
│   │   └── VendorDao.java
│   ├── models/
│   │   ├── Item.java
│   │   └── Vendor.java
│   ├── services/
│   │   └── InventoryService.java
│   └── ui/
│       └── InventoryController.java
├── src/main/resources/
│   └── styles.css
├── src/test/java/com/example/inventory/
│   └── InventoryServiceTest.java
└── .github/workflows/
    └── ci.yml
```

## Getting Started

### Prerequisites

- JDK 21+
- Maven 3.9+
- Git

Verify your installation:

```bash
java -version
mvn -version
git --version
```

### Clone

```bash
git clone https://github.com/saikishore2410/Inventory-Management-System.git
cd Inventory-Management-System/inventory-management-system
```

### Run tests

```bash
mvn clean test
```

### Run the application

```bash
mvn javafx:run
```

## UI

The application includes:

- Dashboard summary cards
- Inventory and Vendors tabs
- Search/filter controls
- Add/Edit/Delete actions
- Stock In / Stock Out controls
- Low-stock visual highlighting
- Modal data-entry forms
- Confirmation and error dialogs
- Status feedback

UI styling is maintained in `src/main/resources/styles.css`.

## Database

SQLite is used as a lightweight local database, so no external database server is required.

The database layer handles connection setup, table initialization, CRUD persistence, and transactional stock updates.

## Business Rules

1. Item and vendor names are required.
2. Stock quantities cannot be negative.
3. Stock In and Stock Out quantities must be greater than zero.
4. Stock Out cannot reduce inventory below zero.
5. Quantity <= 5 is treated as low stock.
6. Failed stock operations are rolled back.

## Testing

Automated tests cover:

- Item creation/retrieval
- Stock In / Stock Out
- Negative-stock prevention
- Low-stock threshold behavior
- Service/database integration

GitHub Actions runs the Maven test/build workflow on repository changes.

## Troubleshooting

**Java version error**

```bash
java -version
```

Make sure JDK 21 is active.

**JavaFX startup problem**

Run through Maven:

```bash
mvn javafx:run
```

**Clean rebuild**

```bash
mvn clean test
```

## Future Enhancements

- Authentication and role-based access
- Inventory transaction history
- CSV/PDF export
- Advanced reports
- Barcode/QR support
- Purchase orders
- Sales/order management
- Supplier analytics
- Backup and restore
- Native desktop packaging

## Author

**Kandi Sai Kishore**

GitHub: https://github.com/saikishore2410

## License

Educational, portfolio, and demonstration project.
