package com.example.a26937_midterm_bank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.a26937_midterm_bank.R;
import com.example.a26937_midterm_bank.models.Customer;
import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    private Context context;
    private List<Customer> customers;

    public CustomerAdapter(Context context, List<Customer> customers) {
        super(context, 0, customers);
        this.context = context;
        this.customers = customers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        }

        Customer customer = customers.get(position);

        TextView tvName = convertView.findViewById(R.id.tvCustomerName);
        TextView tvEmail = convertView.findViewById(R.id.tvCustomerEmail);
        TextView tvPhone = convertView.findViewById(R.id.tvCustomerPhone);

        tvName.setText(customer.getFullName());
        tvEmail.setText(customer.getEmail());
        tvPhone.setText(customer.getPhone());

        return convertView;
    }
}
