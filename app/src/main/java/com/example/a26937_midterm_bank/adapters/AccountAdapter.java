package com.example.a26937_midterm_bank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.a26937_midterm_bank.R;
import com.example.a26937_midterm_bank.models.BankAccount;
import java.util.List;

public class AccountAdapter extends ArrayAdapter<BankAccount> {
    private Context context;
    private List<BankAccount> accounts;

    public AccountAdapter(Context context, List<BankAccount> accounts) {
        super(context, 0, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        }

        BankAccount account = accounts.get(position);

        TextView tvAccountNumber = convertView.findViewById(R.id.tvAccountNumber);
        TextView tvBalance = convertView.findViewById(R.id.tvBalance);
        TextView tvCustomerName = convertView.findViewById(R.id.tvCustomerName);

        tvAccountNumber.setText("Account: " + account.getAccountNumber());
        tvBalance.setText("Balance: $" + String.format("%.2f", account.getBalance()));
        if (account.getCustomer() != null) {
            tvCustomerName.setText("Owner: " + account.getCustomer().getFullName());
        }

        return convertView;
    }
}
