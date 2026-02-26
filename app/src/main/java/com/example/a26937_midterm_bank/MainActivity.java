package com.example.a26937_midterm_bank;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dashboard");
        }

        dbHelper = new DatabaseHelper(this);
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

        loadStatistics();

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

    private void loadStatistics() {
        executorService.execute(() -> {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sessionManager.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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