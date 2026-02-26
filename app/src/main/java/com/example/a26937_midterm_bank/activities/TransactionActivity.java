package com.example.a26937_midterm_bank.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a26937_midterm_bank.R;
import com.example.a26937_midterm_bank.database.DatabaseHelper;
import com.example.a26937_midterm_bank.models.BankAccount;
import com.example.a26937_midterm_bank.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionActivity extends AppCompatActivity {
    private Spinner spinnerAccount, spinnerType;
    private EditText etAmount;
    private CheckBox cbTerms;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private List<BankAccount> accounts;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Transactions");
        }

        dbHelper = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        spinnerAccount = findViewById(R.id.spinnerAccount);
        spinnerType = findViewById(R.id.spinnerType);
        etAmount = findViewById(R.id.etAmount);
        cbTerms = findViewById(R.id.cbTerms);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        loadAccounts();

        btnSave.setOnClickListener(v -> saveTransaction());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAccounts() {
        executorService.execute(() -> {
            List<BankAccount> loadedAccounts = dbHelper.getAllAccountsWithCustomers();
            mainHandler.post(() -> {
                accounts = loadedAccounts;
                List<String> accountNumbers = new ArrayList<>();
                for (BankAccount account : accounts) {
                    accountNumbers.add(account.getAccountNumber());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, accountNumbers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAccount.setAdapter(adapter);
            });
        });
    }

    private void saveTransaction() {
        String amountStr = etAmount.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount < 0) {
            Toast.makeText(this, "Amount cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        if (accounts == null || accounts.isEmpty()) {
            Toast.makeText(this, "No accounts available", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = spinnerAccount.getSelectedItemPosition();
        BankAccount account = accounts.get(selectedPosition);

        if (type.equals("Withdraw") && account.getBalance() < amount) {
            Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Transaction transaction = new Transaction(account.getId(), type, amount, date);
            long result = dbHelper.insertTransaction(transaction);

            if (result > 0) {
                double newBalance = type.equals("Deposit") ? 
                        account.getBalance() + amount : account.getBalance() - amount;
                account.setBalance(newBalance);
                int updateResult = dbHelper.updateAccount(account);

                mainHandler.post(() -> {
                    if (updateResult > 0) {
                        Toast.makeText(this, "Transaction completed successfully", Toast.LENGTH_SHORT).show();
                        loadAccounts();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Transaction saved but balance update failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mainHandler.post(() -> Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void clearFields() {
        etAmount.setText("");
        cbTerms.setChecked(false);
        spinnerAccount.setSelection(0);
        spinnerType.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
