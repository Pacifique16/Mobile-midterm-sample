package com.example.a26937_midterm_bank;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a26937_midterm_bank.activities.AccountActivity;
import com.example.a26937_midterm_bank.activities.AccountListActivity;
import com.example.a26937_midterm_bank.activities.CustomerActivity;
import com.example.a26937_midterm_bank.activities.CustomerListActivity;
import com.example.a26937_midterm_bank.activities.LoginActivity;
import com.example.a26937_midterm_bank.activities.SettingsActivity;
import com.example.a26937_midterm_bank.activities.TransactionActivity;
import com.example.a26937_midterm_bank.database.DatabaseHelper;
import com.example.a26937_midterm_bank.utils.NetworkReceiver;
import com.example.a26937_midterm_bank.utils.SessionManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private NetworkReceiver networkReceiver;
    private TextView tvBankName, tvWelcome, tvTotalCustomers, tvTotalAccounts, tvTotalBalance;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        
        tvToolbarTitle.setText("Dashboard");
        btnBack.setVisibility(android.view.View.GONE);
        btnMenu.setOnClickListener(v -> showMenu(v));

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        tvBankName = findViewById(R.id.tvBankName);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        tvTotalAccounts = findViewById(R.id.tvTotalAccounts);
        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        Button btnManageCustomers = findViewById(R.id.btnManageCustomers);
        Button btnManageAccounts = findViewById(R.id.btnManageAccounts);
        Button btnManageTransactions = findViewById(R.id.btnManageTransactions);
        Button btnSettings = findViewById(R.id.btnSettings);

        SharedPreferences prefs = getSharedPreferences("BankPrefs", MODE_PRIVATE);
        String bankName = prefs.getString("bankName", "Bank Management System");
        tvBankName.setText(bankName);
        tvWelcome.setText("Welcome, " + sessionManager.getFullName());

        // Initialize database on background thread
        executorService.execute(() -> {
            dbHelper = new DatabaseHelper(this);
            mainHandler.post(this::loadStatistics);
        });

        btnManageCustomers.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CustomerActivity.class));
        });

        btnManageAccounts.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
        });

        btnManageTransactions.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TransactionActivity.class));
        });

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        networkReceiver = new NetworkReceiver();
    }

    private void showMenu(android.view.View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(0, 1, 0, "Manage Customers");
        popup.getMenu().add(0, 2, 0, "Manage Accounts");
        popup.getMenu().add(0, 3, 0, "Manage Transactions");
        popup.getMenu().add(0, 4, 0, "Settings");
        popup.getMenu().add(0, 5, 0, "Logout");
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == 1) {
                startActivity(new Intent(MainActivity.this, CustomerActivity.class));
            } else if (id == 2) {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
            } else if (id == 3) {
                startActivity(new Intent(MainActivity.this, TransactionActivity.class));
            } else if (id == 4) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (id == 5) {
                sessionManager.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            return true;
        });
        popup.show();
    }

    private void loadStatistics() {
        executorService.execute(() -> {
            if (dbHelper == null) {
                dbHelper = new DatabaseHelper(this);
            }
            int totalCustomers = dbHelper.getTotalCustomers();
            int totalAccounts = dbHelper.getTotalAccounts();
            double totalBalance = dbHelper.getTotalBalance();

            mainHandler.post(() -> {
                tvTotalCustomers.setText(String.valueOf(totalCustomers));
                tvTotalAccounts.setText(String.valueOf(totalAccounts));
                tvTotalBalance.setText(String.format("$%.2f", totalBalance));
            });
        });
    }

@Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) {
            SharedPreferences prefs = getSharedPreferences("BankPrefs", MODE_PRIVATE);
            String bankName = prefs.getString("bankName", "Bank Management System");
            tvBankName.setText(bankName);
            loadStatistics();

            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkReceiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkReceiver != null) {
            try {
                unregisterReceiver(networkReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}