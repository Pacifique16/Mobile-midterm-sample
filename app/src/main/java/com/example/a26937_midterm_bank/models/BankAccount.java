package com.example.a26937_midterm_bank.models;

public class BankAccount {
    private int id;
    private String accountNumber;
    private double balance;
    private int customerId;
    private String status;
    private Customer customer;

    public BankAccount() {}

    public BankAccount(int id, String accountNumber, double balance, int customerId, String status) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customerId = customerId;
        this.status = status;
    }

    public BankAccount(String accountNumber, double balance, int customerId, String status) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customerId = customerId;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}