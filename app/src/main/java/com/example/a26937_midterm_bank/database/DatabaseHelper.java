package com.example.a26937_midterm_bank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.a26937_midterm_bank.models.BankAccount;
import com.example.a26937_midterm_bank.models.Customer;
import com.example.a26937_midterm_bank.models.Transaction;
import com.example.a26937_midterm_bank.models.User;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BankManagement.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USER = "User";
    private static final String TABLE_CUSTOMER = "Customer";
    private static final String TABLE_ACCOUNT = "BankAccount";
    private static final String TABLE_TRANSACTION = "Transaction";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "fullName TEXT NOT NULL)");

            db.execSQL("CREATE TABLE " + TABLE_CUSTOMER + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fullName TEXT NOT NULL, " +
                    "phone TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "gender TEXT NOT NULL)");

            db.execSQL("CREATE TABLE " + TABLE_ACCOUNT + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "accountNumber TEXT NOT NULL UNIQUE, " +
                    "balance REAL NOT NULL, " +
                    "customerId INTEGER NOT NULL, " +
                    "status TEXT DEFAULT 'ACTIVE', " +
                    "FOREIGN KEY(customerId) REFERENCES " + TABLE_CUSTOMER + "(id))");

            db.execSQL("CREATE TABLE " + TABLE_TRANSACTION + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "accountId INTEGER NOT NULL, " +
                    "type TEXT NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "transactionDate TEXT NOT NULL, " +
                    "FOREIGN KEY(accountId) REFERENCES " + TABLE_ACCOUNT + "(id))");

            db.execSQL("INSERT INTO " + TABLE_USER + " (username, password, fullName) VALUES ('admin', 'admin123', 'Bank Administrator')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User authenticateUser(String username, String password) {
        try {
            createDefaultUserIfNotExists();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE username = ? AND password = ?", 
                    new String[]{username, password});
            if (cursor.moveToFirst()) {
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                cursor.close();
                return user;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createDefaultUserIfNotExists() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{TABLE_USER});
            if (!cursor.moveToFirst()) {
                db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL, " +
                        "fullName TEXT NOT NULL)");
            }
            cursor.close();

            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USER, null);
            if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
                db.execSQL("INSERT INTO " + TABLE_USER + " (username, password, fullName) VALUES ('admin', 'admin123', 'Bank Administrator')");
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalCustomers() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CUSTOMER, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                cursor.close();
                return count;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalAccounts() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ACCOUNT, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                cursor.close();
                return count;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalBalance() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT SUM(balance) FROM " + TABLE_ACCOUNT, null);
            if (cursor.moveToFirst()) {
                double total = cursor.getDouble(0);
                cursor.close();
                return total;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long insertCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fullName", customer.getFullName());
            values.put("phone", customer.getPhone());
            values.put("email", customer.getEmail());
            values.put("gender", customer.getGender());
            return db.insert(TABLE_CUSTOMER, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int updateCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fullName", customer.getFullName());
            values.put("phone", customer.getPhone());
            values.put("email", customer.getEmail());
            values.put("gender", customer.getGender());
            return db.update(TABLE_CUSTOMER, values, "id = ?", new String[]{String.valueOf(customer.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteCustomer(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_CUSTOMER, "id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CUSTOMER, null);
            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    );
                    customers.add(customer);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public Customer getCustomerById(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CUSTOMER + " WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                Customer customer = new Customer(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                cursor.close();
                return customer;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long insertAccount(BankAccount account) {
        try {
            ensureAccountStatusColumn();
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("accountNumber", account.getAccountNumber());
            values.put("balance", account.getBalance());
            values.put("customerId", account.getCustomerId());
            values.put("status", account.getStatus() != null ? account.getStatus() : "ACTIVE");
            return db.insert(TABLE_ACCOUNT, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void ensureAccountStatusColumn() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_ACCOUNT + ")", null);
            boolean hasStatus = false;
            while (cursor.moveToNext()) {
                String columnName = cursor.getString(cursor.getColumnIndex("name"));
                if ("status".equals(columnName)) {
                    hasStatus = true;
                    break;
                }
            }
            cursor.close();
            if (!hasStatus) {
                db.execSQL("ALTER TABLE " + TABLE_ACCOUNT + " ADD COLUMN status TEXT DEFAULT 'ACTIVE'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int updateAccount(BankAccount account) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("accountNumber", account.getAccountNumber());
            values.put("balance", account.getBalance());
            values.put("customerId", account.getCustomerId());
            if (account.getStatus() != null) {
                values.put("status", account.getStatus());
            }
            return db.update(TABLE_ACCOUNT, values, "id = ?", new String[]{String.valueOf(account.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteAccount(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_ACCOUNT, "id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<BankAccount> getAllAccountsWithCustomers() {
        List<BankAccount> accounts = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT a.id, a.accountNumber, a.balance, a.customerId, a.status, " +
                    "c.fullName, c.phone, c.email, c.gender " +
                    "FROM " + TABLE_ACCOUNT + " a " +
                    "INNER JOIN " + TABLE_CUSTOMER + " c ON a.customerId = c.id";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    BankAccount account = new BankAccount(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getDouble(2),
                            cursor.getInt(3),
                            cursor.getString(4)
                    );
                    Customer customer = new Customer(
                            cursor.getInt(3),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8)
                    );
                    account.setCustomer(customer);
                    accounts.add(account);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public BankAccount getAccountById(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                BankAccount account = new BankAccount(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
                cursor.close();
                return account;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long insertTransaction(Transaction transaction) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("accountId", transaction.getAccountId());
            values.put("type", transaction.getType());
            values.put("amount", transaction.getAmount());
            values.put("transactionDate", transaction.getTransactionDate());
            return db.insert(TABLE_TRANSACTION, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int updateTransaction(Transaction transaction) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("accountId", transaction.getAccountId());
            values.put("type", transaction.getType());
            values.put("amount", transaction.getAmount());
            values.put("transactionDate", transaction.getTransactionDate());
            return db.update(TABLE_TRANSACTION, values, "id = ?", new String[]{String.valueOf(transaction.getId())});
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteTransaction(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_TRANSACTION, "id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION, null);
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getString(4)
                    );
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
