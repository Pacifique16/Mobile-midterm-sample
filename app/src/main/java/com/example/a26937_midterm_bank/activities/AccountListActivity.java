package com.example.a26937_midterm_bank.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a26937_midterm_bank.R;
import com.example.a26937_midterm_bank.adapters.AccountAdapter;
import com.example.a26937_midterm_bank.database.DatabaseHelper;
import com.example.a26937_midterm_bank.models.BankAccount;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountListActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Account List");
        }

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewAccounts);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        loadAccounts();
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
            List<BankAccount> accounts = dbHelper.getAllAccountsWithCustomers();
            mainHandler.post(() -> {
                AccountAdapter adapter = new AccountAdapter(this, accounts);
                listView.setAdapter(adapter);
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
