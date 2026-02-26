package com.example.a26937_midterm_bank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a26937_midterm_bank.R;
import com.example.a26937_midterm_bank.adapters.CustomerAdapter;
import com.example.a26937_midterm_bank.database.DatabaseHelper;
import com.example.a26937_midterm_bank.models.Customer;
import com.example.a26937_midterm_bank.utils.CSVExporter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerListActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;
    private Button btnExportCSV;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Customer List");
        }

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewCustomers);
        btnExportCSV = findViewById(R.id.btnExportCSV);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        loadCustomers();

        btnExportCSV.setOnClickListener(v -> exportToCSV());
        
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Customer customer = (Customer) parent.getItemAtPosition(position);
            Intent intent = new Intent(CustomerListActivity.this, CustomerActivity.class);
            intent.putExtra("customer_id", customer.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
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
            List<Customer> customers = dbHelper.getAllCustomers();
            mainHandler.post(() -> {
                CustomerAdapter adapter = new CustomerAdapter(this, customers);
                listView.setAdapter(adapter);
            });
        });
    }

    private void exportToCSV() {
        executorService.execute(() -> {
            List<Customer> customers = dbHelper.getAllCustomers();
            if (customers.isEmpty()) {
                mainHandler.post(() -> Toast.makeText(this, "No customers to export", Toast.LENGTH_SHORT).show());
                return;
            }

            boolean success = CSVExporter.exportCustomersToCSV(this, customers);
            mainHandler.post(() -> {
                if (success) {
                    Toast.makeText(this, "Exported to " + getFilesDir() + "/customers.csv", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
