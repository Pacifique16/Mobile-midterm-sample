package com.example.a26937_midterm_bank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
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
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        
        tvToolbarTitle.setText("Customer List");
        btnBack.setOnClickListener(v -> finish());
        btnMenu.setOnClickListener(v -> showMenu(v));

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewCustomers);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            dbHelper = new DatabaseHelper(this);
            mainHandler.post(this::loadCustomers);
        });
        
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Customer customer = (Customer) parent.getItemAtPosition(position);
            Intent intent = new Intent(CustomerListActivity.this, CustomerActivity.class);
            intent.putExtra("customer_id", customer.getId());
            startActivity(intent);
        });
    }

    private void showMenu(android.view.View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(0, 1, 0, "Export to CSV");
        popup.setOnMenuItemClickListener(item -> {
            exportToCSV();
            return true;
        });
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }

private void loadCustomers() {
        executorService.execute(() -> {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
            List<Customer> customers = dbHelper.getAllCustomers();
            mainHandler.post(() -> {
                CustomerAdapter adapter = new CustomerAdapter(this, customers);
                listView.setAdapter(adapter);
            });
        });
    }

    private void exportToCSV() {
        executorService.execute(() -> {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
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
