# Inventory Management System

This project is an Inventory Management System built using JavaFX and SQLite. It provides a user-friendly interface for managing inventory, tracking stock entries, and handling vendor information.

## Features

- **Stock Management**: Add, update, and track stock entries. Generate reports for low-stock items.
- **Vendor Management**: Manage vendor information, including adding and updating vendor details.
- **Low-Stock Alerts**: Receive notifications for items that are low in stock to ensure timely restocking.

## Technologies Used

- **Java**: The primary programming language for the application.
- **JavaFX**: For building the graphical user interface.
- **SQLite**: For database management and storage.

## Project Structure

```
inventory-management-system
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── inventory
│   │   │               ├── MainApp.java
│   │   │               ├── controllers
│   │   │               │   ├── DashboardController.java
│   │   │               │   ├── StockController.java
│   │   │               │   └── VendorController.java
│   │   │               ├── models
│   │   │               │   ├── Item.java
│   │   │               │   ├── StockEntry.java
│   │   │               │   └── Vendor.java
│   │   │               ├── dao
│   │   │               │   ├── Database.java
│   │   │               │   ├── ItemDao.java
│   │   │               │   └── VendorDao.java
│   │   │               ├── services
│   │   │               │   ├── InventoryService.java
│   │   │               │   └── AlertService.java
│   │   │               └── util
│   │   │                   └── Config.java
│   │   └── resources
│   │       ├── fxml
│   │       │   ├── dashboard.fxml
│   │       │   ├── stock.fxml
│   │       │   └── vendor.fxml
│   │       ├── css
│   │       │   └── styles.css
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── inventory
│                       ├── InventoryServiceTest.java
│                       └── DatabaseTest.java
├── pom.xml
├── .gitignore
└── README.md
```

## Setup Instructions

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Ensure you have Java and Maven installed.
4. Run `mvn clean install` to build the project.
5. Run the application using `mvn javafx:run`.

## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue for any suggestions or improvements.