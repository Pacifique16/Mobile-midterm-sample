package com.example.a26937_midterm_bank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a26937_midterm_bank.R;
import com.example.a26937_midterm_bank.database.DatabaseHelper;
import com.example.a26937_midterm_bank.models.BankAccount;
import com.example.a26937_midterm_bank.models.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountActivity extends AppCompatActivity {
    private EditText etAccountNumber, etBalance;
    private Spinner spinnerCustomer;
    private Button btnSave, btnViewList;
    private DatabaseHelper dbHelper;
    private List<Customer> customers;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Accounts");
        }

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Initialize database on background
        executorService.execute(() -> {
            dbHelper = new DatabaseHelper(this);
            mainHandler.post(this::loadCustomers);
        });

        etAccountNumber = findViewById(R.id.etAccountNumber);
        etBalance = findViewById(R.id.etBalance);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        btnSave = findViewById(R.id.btnSave);
        btnViewList = findViewById(R.id.btnViewList);

        etAccountNumber.setEnabled(false);
        generateAccountNumber();
        loadCustomers();

        btnSave.setOnClickListener(v -> saveAccount());
        btnViewList.setOnClickListener(v -> {
            startActivity(new Intent(AccountActivity.this, AccountListActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCustomers() {
        executorService.execute(() -> {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
            List<Customer> loadedCustomers = dbHelper.getAllCustomers();
            mainHandler.post(() -> {
                customers = loadedCustomers;
                List<String> customerNames = new ArrayList<>();
                for (Customer customer : customers) {
                    customerNames.add(customer.getFullName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, customerNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCustomer.setAdapter(adapter);
            });
        });
    }

    private void generateAccountNumber() {
        String accountNumber = "ACC" + System.currentTimeMillis();
        etAccountNumber.setText(accountNumber);
    }

    private void saveAccount() {
        String accountNumber = etAccountNumber.getText().toString().trim();
        String balanceStr = etBalance.getText().toString().trim();

        if (balanceStr.isEmpty()) {
            Toast.makeText(this, "Balance is required", Toast.LENGTH_SHORT).show();
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid balance amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (balance < 0) {
            Toast.makeText(this, "Balance cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customers == null || customers.isEmpty()) {
            Toast.makeText(this, "No customers available. Add a customer first.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = spinnerCustomer.getSelectedItemPosition();
        int customerId = customers.get(selectedPosition).getId();

        executorService.execute(() -> {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
            BankAccount account = new BankAccount(accountNumber, balance, customerId, "ACTIVE");
            long result = dbHelper.insertAccount(account);

            mainHandler.post(() -> {
                if (result > 0) {
                    Toast.makeText(this, "Account saved successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(this, "Failed to save account", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void clearFields() {
        generateAccountNumber();
        etBalance.setText("");
        spinnerCustomer.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}