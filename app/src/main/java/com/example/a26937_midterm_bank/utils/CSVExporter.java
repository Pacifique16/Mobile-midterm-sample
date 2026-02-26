package com.example.a26937_midterm_bank.utils;

import android.content.Context;
import com.example.a26937_midterm_bank.models.Customer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {
    public static boolean exportCustomersToCSV(Context context, List<Customer> customers) {
        try {
            File file = new File(context.getFilesDir(), "customers.csv");
            FileWriter writer = new FileWriter(file);
            
            writer.append("CustomerID,FullName,Phone,Email\n");
            
            for (Customer customer : customers) {
                writer.append(String.valueOf(customer.getId())).append(",");
                writer.append(customer.getFullName()).append(",");
                writer.append(customer.getPhone()).append(",");
                writer.append(customer.getEmail()).append("\n");
            }
            
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
