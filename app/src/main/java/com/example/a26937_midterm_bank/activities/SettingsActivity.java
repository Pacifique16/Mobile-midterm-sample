package com.example.a26937_midterm_bank.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a26937_midterm_bank.R;

public class SettingsActivity extends AppCompatActivity {
    private EditText etBankName;
    private Spinner spinnerCurrency;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        etBankName = findViewById(R.id.etBankName);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        btnSave = findViewById(R.id.btnSaveSettings);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("BankPrefs", MODE_PRIVATE);
        String bankName = prefs.getString("bankName", "");
        String currency = prefs.getString("currency", "USD");

        etBankName.setText(bankName);
        
        ArrayAdapter adapter = (ArrayAdapter) spinnerCurrency.getAdapter();
        int position = adapter.getPosition(currency);
        spinnerCurrency.setSelection(position);
    }

    private void saveSettings() {
        String bankName = etBankName.getText().toString().trim();
        String currency = spinnerCurrency.getSelectedItem().toString();

        if (bankName.isEmpty()) {
            Toast.makeText(this, "Bank name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("BankPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("bankName", bankName);
        editor.putString("currency", currency);
        editor.apply();

        Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
