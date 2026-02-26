# 26937_midterm_bank - Bank Management Android Application

## Project Overview
A comprehensive Bank Management Android Application that manages customers, bank accounts, and transactions with a clean, organized architecture.

## Student Information
- **Student ID**: 26937
- **Project Name**: 26937_midterm_bank

## Features

### 1. Multi-Screen Navigation (15 Marks)
- **MainActivity (Dashboard)**: Central hub with navigation buttons and bank logo
- **CustomerActivity**: Add/Edit customer information
- **CustomerListActivity**: View all customers with custom ListView adapter
- **AccountActivity**: Create/Manage bank accounts
- **AccountListActivity**: View all accounts with customer names (JOIN query)
- **TransactionActivity**: Perform deposits and withdrawals
- **SettingsActivity**: Configure bank name and currency

### 2. Model Classes (10 Marks)
Three POJO classes reflecting database structure:
- **Customer**: id, fullName, phone, email, gender
- **BankAccount**: id, accountNumber, balance, customerId (FK to Customer)
- **Transaction**: id, accountId (FK to BankAccount), type, amount, transactionDate

### 3. UI Design & Validation (15 Marks)
**UI Components Used:**
- EditText (for text input)
- TextView (for labels and display)
- Spinner (for gender, transaction type, currency selection)
- CheckBox (for terms agreement in transactions)
- ImageView (bank logo on dashboard)
- ListView with custom adapters (CustomerAdapter, AccountAdapter)
- Button (navigation and actions)

**Validation Implemented:**
- No empty fields validation
- Email must contain "@" symbol
- Balance cannot be negative
- Insufficient balance check for withdrawals
- Terms agreement required for transactions

### 4. SQLite Database with CRUD (25 Marks)
**Database Structure:**
- 3 Tables: Customer, BankAccount, Transaction
- Primary Keys: Auto-increment IDs
- Foreign Keys: 
  - BankAccount.customerId → Customer.id
  - Transaction.accountId → BankAccount.id

**CRUD Operations:**
- **Create**: Insert customers, accounts, and transactions
- **Read**: Retrieve all records with JOIN queries
- **Update**: Modify customer and account information
- **Delete**: Remove records from database
- **JOIN Query**: AccountListActivity displays customer names with accounts

**Exception Handling:**
- Try-catch blocks in all database operations
- Proper error handling and user feedback

### 5. SharedPreferences (10 Marks)
**SettingsActivity Features:**
- Save bank name
- Save currency type
- Persistent storage across app sessions
- Bank name displayed on Dashboard

### 6. CSV Export (10 Marks)
**Export Functionality:**
- Export customer list to CSV file
- Format: CustomerID,FullName,Phone,Email
- Saved to internal storage: /data/data/com.example.a26937_midterm_bank/files/customers.csv
- User notification with file path

### 7. Extra Feature - BroadcastReceiver (10 Marks)
**NetworkReceiver Implementation:**
- Detects internet connection status changes
- Shows Toast message when connection status changes
- Registered in MainActivity lifecycle
- Permission: ACCESS_NETWORK_STATE

## Project Structure

```
com.example.a26937_midterm_bank/
├── activities/
│   ├── AccountActivity.java
│   ├── AccountListActivity.java
│   ├── CustomerActivity.java
│   ├── CustomerListActivity.java
│   ├── SettingsActivity.java
│   └── TransactionActivity.java
├── adapters/
│   ├── AccountAdapter.java
│   └── CustomerAdapter.java
├── database/
│   └── DatabaseHelper.java
├── models/
│   ├── BankAccount.java
│   ├── Customer.java
│   └── Transaction.java
├── utils/
│   ├── CSVExporter.java
│   └── NetworkReceiver.java
└── MainActivity.java
```

## Installation & Setup

1. Clone the repository
2. Open project in Android Studio
3. Sync Gradle files
4. Run on emulator or physical device (API 24+)

## Usage Guide

### Adding a Customer
1. From Dashboard, tap "Manage Customers"
2. Fill in all required fields (Name, Phone, Email, Gender)
3. Tap "Save Customer"
4. View customer list by tapping "View Customer List"

### Creating a Bank Account
1. From Dashboard, tap "Manage Accounts"
2. Enter account number and initial balance
3. Select customer from dropdown
4. Tap "Save Account"
5. View accounts with customer names in "View Account List"

### Making Transactions
1. From Dashboard, tap "Manage Transactions"
2. Select account from dropdown
3. Choose transaction type (Deposit/Withdraw)
4. Enter amount
5. Check terms agreement checkbox
6. Tap "Complete Transaction"

### Exporting Customer Data
1. Navigate to Customer List
2. Tap "Export to CSV"
3. File saved to internal storage with confirmation message

### Configuring Settings
1. From Dashboard, tap "Settings"
2. Enter bank name
3. Select currency type
4. Tap "Save Settings"
5. Bank name will appear on Dashboard

## Technical Specifications

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 36
- **Language**: Java
- **Database**: SQLite
- **Architecture**: MVC Pattern

## Validation Rules

1. **Customer Validation**:
   - All fields required
   - Email must contain "@"

2. **Account Validation**:
   - Account number required
   - Balance cannot be negative
   - Customer must be selected

3. **Transaction Validation**:
   - Amount required and must be positive
   - Terms agreement required
   - Sufficient balance for withdrawals

## Database Schema

### Customer Table
```sql
CREATE TABLE Customer (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fullName TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT NOT NULL,
    gender TEXT NOT NULL
)
```

### BankAccount Table
```sql
CREATE TABLE BankAccount (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    accountNumber TEXT NOT NULL UNIQUE,
    balance REAL NOT NULL,
    customerId INTEGER NOT NULL,
    FOREIGN KEY(customerId) REFERENCES Customer(id)
)
```

### Transaction Table
```sql
CREATE TABLE Transaction (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    accountId INTEGER NOT NULL,
    type TEXT NOT NULL,
    amount REAL NOT NULL,
    transactionDate TEXT NOT NULL,
    FOREIGN KEY(accountId) REFERENCES BankAccount(id)
)
```

## Code Organization & Cleanliness (5 Marks)

- Well-structured packages (activities, adapters, database, models, utils)
- Meaningful class and variable names
- Proper code formatting and indentation
- Comprehensive comments where necessary
- Separation of concerns (MVC pattern)
- Reusable components (custom adapters)

## Testing

The application has been tested for:
- CRUD operations on all entities
- Data validation and error handling
- Navigation between activities
- SharedPreferences persistence
- CSV export functionality
- BroadcastReceiver for network changes
- JOIN queries displaying correct data

## Future Enhancements

- Transaction history view
- Account balance updates in real-time
- Search and filter functionality
- Data backup and restore
- User authentication
- Multiple bank branch support

## License

This project is created for educational purposes as part of Mobile Programming coursework.

## Contact

For any queries regarding this project, please contact through the university portal.

---

**Developed by**: Student 26937  
**Course**: Mobile Programming  
**Institution**: AUCA  
**Semester**: 8
