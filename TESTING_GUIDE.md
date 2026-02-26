# Testing Guide - 26937_midterm_bank

## Pre-Testing Setup

1. Open Android Studio
2. Open the project: `26937_midterm_bank`
3. Wait for Gradle sync to complete
4. Connect Android device or start emulator (API 24+)
5. Click Run button or press Shift+F10

## Testing Checklist

### ✅ 1. Dashboard (MainActivity)
- [ ] App launches successfully
- [ ] Bank logo (app icon) is displayed
- [ ] Bank name shows "Bank Management System" (default)
- [ ] All 4 buttons are visible:
  - Manage Customers
  - Manage Accounts
  - Manage Transactions
  - Settings

### ✅ 2. Customer Management

#### Add Customer (CustomerActivity)
- [ ] Click "Manage Customers" from dashboard
- [ ] Try saving with empty fields → Should show error
- [ ] Enter name: "John Doe"
- [ ] Enter phone: "0788123456"
- [ ] Enter invalid email: "johndoe" → Should show error
- [ ] Enter valid email: "john@example.com"
- [ ] Select gender: "Male"
- [ ] Click "Save Customer" → Should show success message
- [ ] Fields should clear after save

#### View Customer List (CustomerListActivity)
- [ ] Click "View Customer List"
- [ ] Customer "John Doe" should appear in list
- [ ] Customer details displayed: name, email, phone
- [ ] Click "Export to CSV"
- [ ] Should show success message with file path

#### Add More Customers
- [ ] Go back and add at least 2 more customers:
  - Jane Smith, 0788234567, jane@example.com, Female
  - Bob Wilson, 0788345678, bob@example.com, Male

### ✅ 3. Account Management

#### Add Account (AccountActivity)
- [ ] Go to dashboard, click "Manage Accounts"
- [ ] Try saving with empty fields → Should show error
- [ ] Enter account number: "ACC001"
- [ ] Enter negative balance: "-100" → Should show error
- [ ] Enter valid balance: "1000"
- [ ] Select customer: "John Doe"
- [ ] Click "Save Account" → Should show success
- [ ] Fields should clear

#### View Account List (AccountListActivity)
- [ ] Click "View Account List"
- [ ] Account "ACC001" should appear
- [ ] Should display: Account number, Balance, Owner name (JOIN query)
- [ ] Owner should show "John Doe"

#### Add More Accounts
- [ ] Add account for Jane: ACC002, balance 2000
- [ ] Add account for Bob: ACC003, balance 500

### ✅ 4. Transaction Management

#### Deposit Transaction
- [ ] Go to dashboard, click "Manage Transactions"
- [ ] Select account: "ACC001"
- [ ] Select type: "Deposit"
- [ ] Enter amount: "500"
- [ ] Try without checking terms → Should show error
- [ ] Check "I agree to terms and conditions"
- [ ] Click "Complete Transaction" → Should show success
- [ ] Go to Account List → ACC001 balance should be 1500

#### Withdrawal Transaction
- [ ] Go to Manage Transactions
- [ ] Select account: "ACC003" (Bob's account with 500)
- [ ] Select type: "Withdraw"
- [ ] Enter amount: "1000" (more than balance)
- [ ] Check terms
- [ ] Click "Complete Transaction" → Should show "Insufficient balance"
- [ ] Change amount to "200"
- [ ] Click "Complete Transaction" → Should succeed
- [ ] Check Account List → ACC003 balance should be 300

### ✅ 5. Settings & SharedPreferences

#### Save Settings
- [ ] Go to dashboard, click "Settings"
- [ ] Enter bank name: "AUCA Bank"
- [ ] Select currency: "RWF"
- [ ] Click "Save Settings" → Should show success
- [ ] Go back to dashboard
- [ ] Bank name should now show "AUCA Bank"

#### Test Persistence
- [ ] Close the app completely
- [ ] Reopen the app
- [ ] Dashboard should still show "AUCA Bank"
- [ ] Go to Settings → Should show saved values

### ✅ 6. CSV Export

#### Verify CSV File
- [ ] Go to Customer List
- [ ] Click "Export to CSV"
- [ ] Note the file path shown in Toast message
- [ ] Use Device File Explorer in Android Studio:
  - Navigate to: /data/data/com.example.a26937_midterm_bank/files/
  - Find customers.csv
  - Right-click → Save As
  - Open in text editor
- [ ] Verify format: CustomerID,FullName,Phone,Email
- [ ] Verify all customers are listed

### ✅ 7. BroadcastReceiver (Network Status)

#### Test Network Changes
- [ ] Make sure app is on dashboard
- [ ] Turn OFF WiFi/Mobile Data
- [ ] Should see Toast: "No Internet Connection"
- [ ] Turn ON WiFi/Mobile Data
- [ ] Should see Toast: "Internet Connected"
- [ ] Test multiple times to verify

### ✅ 8. Validation Testing

#### Email Validation
- [ ] Try emails without "@" → Should fail
- [ ] Try valid emails → Should succeed

#### Balance Validation
- [ ] Try negative balance → Should fail
- [ ] Try zero balance → Should succeed
- [ ] Try positive balance → Should succeed

#### Empty Fields
- [ ] Try saving any form with empty fields → Should fail
- [ ] Fill all fields → Should succeed

#### Transaction Validation
- [ ] Withdraw more than balance → Should fail
- [ ] Withdraw within balance → Should succeed
- [ ] Deposit any amount → Should succeed
- [ ] Transaction without terms agreement → Should fail

### ✅ 9. Database Operations (CRUD)

#### Create (INSERT)
- [x] Customers inserted successfully
- [x] Accounts inserted successfully
- [x] Transactions inserted successfully

#### Read (SELECT)
- [x] Customer list displays correctly
- [x] Account list displays correctly
- [x] JOIN query shows customer names with accounts

#### Update
- [x] Account balance updates after transactions

#### Delete
- Note: Delete operations are implemented in DatabaseHelper
- Can be tested by adding delete buttons in future

### ✅ 10. UI Components Verification

- [x] EditText - Used in all forms
- [x] TextView - Used for labels and display
- [x] Spinner - Gender, Transaction Type, Currency
- [x] CheckBox - Terms agreement
- [x] ImageView - Bank logo on dashboard
- [x] ListView - Customer and Account lists
- [x] Button - All navigation and action buttons

## Expected Results Summary

| Feature | Expected Result |
|---------|----------------|
| Add Customer | Success with validation |
| View Customers | List displays with custom adapter |
| Export CSV | File created in internal storage |
| Add Account | Success with customer selection |
| View Accounts | Shows customer names (JOIN) |
| Deposit | Balance increases |
| Withdraw | Balance decreases, checks sufficient funds |
| Settings | Saves and persists bank name & currency |
| Network Receiver | Toast on connectivity change |
| Validation | All validations work correctly |

## Common Issues & Solutions

### Issue: App crashes on launch
**Solution**: Check AndroidManifest.xml has all activities declared

### Issue: Database not working
**Solution**: Uninstall app and reinstall to recreate database

### Issue: Network receiver not working
**Solution**: Check ACCESS_NETWORK_STATE permission in manifest

### Issue: CSV file not found
**Solution**: Use Device File Explorer in Android Studio to locate file

### Issue: Gradle sync fails
**Solution**: Check internet connection and sync again

## Performance Testing

- [ ] App launches within 3 seconds
- [ ] Navigation between screens is smooth
- [ ] Database operations complete quickly
- [ ] No memory leaks or crashes
- [ ] ListView scrolls smoothly

## Final Verification

- [ ] All 7 activities work correctly
- [ ] All 3 model classes used
- [ ] All UI components present
- [ ] All validations working
- [ ] CRUD operations functional
- [ ] JOIN query working
- [ ] SharedPreferences persisting
- [ ] CSV export successful
- [ ] BroadcastReceiver functional
- [ ] Code is clean and organized

---

**Testing Status**: Ready for comprehensive testing
**Estimated Testing Time**: 30-45 minutes
**Recommended**: Test on both emulator and physical device
