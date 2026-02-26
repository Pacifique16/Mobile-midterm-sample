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
import com.example.a26937_midterm_bank.models.Customer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerActivity extends AppCompatActivity {
    private EditText etFullName, etPhone, etEmail;
    private Spinner spinnerGender;
    private Button btnSave, btnViewList;
    private DatabaseHelper dbHelper;
    private Customer editingCustomer;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Customers");
        }

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Initialize database on background
        executorService.execute(() -> {
            dbHelper = new DatabaseHelper(this);
        });

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSave);
        btnViewList = findViewById(R.id.btnViewList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        loadCustomerData();

        btnSave.setOnClickListener(v -> saveCustomer());
        btnViewList.setOnClickListener(v -> {
            startActivity(new Intent(CustomerActivity.this, CustomerListActivity.class));
        });
    }

    private void loadCustomerData() {
        Intent intent = getIntent();
        if (intent.hasExtra("customer_id")) {
            int customerId = intent.getIntExtra("customer_id", -1);
            executorService.execute(() -> {
                if (dbHelper == null) {
                    dbHelper = new DatabaseHelper(this);
                }
                Customer customer = dbHelper.getCustomerById(customerId);
                mainHandler.post(() -> {
                    editingCustomer = customer;
                    if (editingCustomer != null) {
                        etFullName.setText(editingCustomer.getFullName());
                        etPhone.setText(editingCustomer.getPhone());
                        etEmail.setText(editingCustomer.getEmail());
                        
                        ArrayAdapter adapter = (ArrayAdapter) spinnerGender.getAdapter();
                        int position = adapter.getPosition(editingCustomer.getGender());
                        spinnerGender.setSelection(position);
                        
                        btnSave.setText("Update Customer");
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle("Edit Customer");
                        }
                    }
                });
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCustomer() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
            if (editingCustomer != null) {
                editingCustomer.setFullName(fullName);
                editingCustomer.setPhone(phone);
                editingCustomer.setEmail(email);
                editingCustomer.setGender(gender);
                int result = dbHelper.updateCustomer(editingCustomer);
                mainHandler.post(() -> {
                    if (result > 0) {
                        Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update customer", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Customer customer = new Customer(fullName, phone, email, gender);
                long result = dbHelper.insertCustomer(customer);
                mainHandler.post(() -> {
                    if (result > 0) {
                        Toast.makeText(this, "Customer saved successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Failed to save customer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void clearFields() {
        etFullName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        spinnerGender.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
