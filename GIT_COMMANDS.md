# Git Commands to Push to GitLab

## Initial Setup (Run these commands in order)

### 1. Initialize Git Repository
```bash
cd "c:\Users\Pacifique Harerimana\OneDrive\Desktop\AUCA\Semester 8\Mobile Programming\26937_midterm_bank"
git init
```

### 2. Configure Git (if not already done)
```bash
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

### 3. Add Remote Repository
```bash
git remote add origin https://gitlab.com/harerimanapacifique95/26937_midterm_bank.git
```

### 4. Create Multiple Commits (As Required - At least 10 commits)

#### Commit 1: Initial project setup
```bash
git add .gitignore README.md
git commit -m "Initial commit: Add .gitignore and README.md"
```

#### Commit 2: Add model classes
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/models/
git commit -m "Add model classes: Customer, BankAccount, Transaction"
```

#### Commit 3: Add database helper
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/database/
git commit -m "Add DatabaseHelper with CRUD operations and JOIN queries"
```

#### Commit 4: Add custom adapters
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/adapters/
git commit -m "Add custom adapters for Customer and Account ListViews"
```

#### Commit 5: Add utility classes
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/utils/
git commit -m "Add CSVExporter and NetworkReceiver utilities"
```

#### Commit 6: Update MainActivity
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/MainActivity.java
git commit -m "Update MainActivity as Dashboard with navigation"
```

#### Commit 7: Add Customer activities
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/activities/CustomerActivity.java
git add app/src/main/java/com/example/a26937_midterm_bank/activities/CustomerListActivity.java
git commit -m "Add CustomerActivity and CustomerListActivity with validation"
```

#### Commit 8: Add Account activities
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/activities/AccountActivity.java
git add app/src/main/java/com/example/a26937_midterm_bank/activities/AccountListActivity.java
git commit -m "Add AccountActivity and AccountListActivity with JOIN query"
```

#### Commit 9: Add Transaction and Settings activities
```bash
git add app/src/main/java/com/example/a26937_midterm_bank/activities/TransactionActivity.java
git add app/src/main/java/com/example/a26937_midterm_bank/activities/SettingsActivity.java
git commit -m "Add TransactionActivity and SettingsActivity with SharedPreferences"
```

#### Commit 10: Add all layout files
```bash
git add app/src/main/res/layout/
git commit -m "Add all activity layouts and custom list item layouts"
```

#### Commit 11: Update strings and manifest
```bash
git add app/src/main/res/values/strings.xml
git add app/src/main/AndroidManifest.xml
git commit -m "Update strings.xml with arrays and AndroidManifest with activities"
```

#### Commit 12: Add remaining configuration files
```bash
git add app/build.gradle.kts gradle/ settings.gradle.kts build.gradle.kts
git commit -m "Add Gradle configuration files"
```

### 5. Push to GitLab
```bash
git branch -M main
git push -u origin main
```

## Alternative: Single Command to Add All and Push

If you want to do it quickly (but less organized):

```bash
git add .
git commit -m "Complete Bank Management Android Application"
git branch -M main
git push -u origin main
```

## Verify Your Commits

After pushing, check your commits:
```bash
git log --oneline
```

## If Repository Already Exists on GitLab

If you need to pull first:
```bash
git pull origin main --allow-unrelated-histories
git push -u origin main
```

## Notes

- Make sure you have at least 10 commits as required
- Each commit should represent a logical unit of work
- Use meaningful commit messages
- The project is now ready to be pushed to: https://gitlab.com/harerimanapacifique95/26937_midterm_bank
