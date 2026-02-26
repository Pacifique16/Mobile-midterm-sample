# Quick Git Push Script
# Copy and paste these commands one by one in Git Bash or Terminal

# Navigate to project directory
cd "c:\Users\Pacifique Harerimana\OneDrive\Desktop\AUCA\Semester 8\Mobile Programming\26937_midterm_bank"

# Initialize Git (if not already done)
git init

# Configure Git (replace with your details)
git config user.name "Pacifique Harerimana"
git config user.email "harerimanapacifique95@gmail.com"

# Add remote repository
git remote add origin https://gitlab.com/harerimanapacifique95/26937_midterm_bank.git

# OR if remote already exists, update it:
# git remote set-url origin https://gitlab.com/harerimanapacifique95/26937_midterm_bank.git

# ============================================
# OPTION 1: Multiple Commits (Recommended - Shows development process)
# ============================================

# Commit 1: Documentation
git add README.md .gitignore IMPLEMENTATION_SUMMARY.md TESTING_GUIDE.md GIT_COMMANDS.md
git commit -m "docs: Add project documentation and README"

# Commit 2: Model classes
git add app/src/main/java/com/example/a26937_midterm_bank/models/
git commit -m "feat: Add model classes (Customer, BankAccount, Transaction)"

# Commit 3: Database helper
git add app/src/main/java/com/example/a26937_midterm_bank/database/
git commit -m "feat: Implement DatabaseHelper with CRUD operations and JOIN queries"

# Commit 4: Utility classes
git add app/src/main/java/com/example/a26937_midterm_bank/utils/
git commit -m "feat: Add CSVExporter and NetworkReceiver utilities"

# Commit 5: Custom adapters
git add app/src/main/java/com/example/a26937_midterm_bank/adapters/
git commit -m "feat: Implement custom ListView adapters for Customer and Account"

# Commit 6: MainActivity
git add app/src/main/java/com/example/a26937_midterm_bank/MainActivity.java
git commit -m "feat: Update MainActivity as Dashboard with navigation and BroadcastReceiver"

# Commit 7: Customer activities
git add app/src/main/java/com/example/a26937_midterm_bank/activities/CustomerActivity.java
git add app/src/main/java/com/example/a26937_midterm_bank/activities/CustomerListActivity.java
git commit -m "feat: Add Customer management activities with validation and CSV export"

# Commit 8: Account activities
git add app/src/main/java/com/example/a26937_midterm_bank/activities/AccountActivity.java
git add app/src/main/java/com/example/a26937_midterm_bank/activities/AccountListActivity.java
git commit -m "feat: Add Account management activities with JOIN query display"

# Commit 9: Transaction and Settings
git add app/src/main/java/com/example/a26937_midterm_bank/activities/TransactionActivity.java
git add app/src/main/java/com/example/a26937_midterm_bank/activities/SettingsActivity.java
git commit -m "feat: Add Transaction activity and Settings with SharedPreferences"

# Commit 10: Main layouts
git add app/src/main/res/layout/activity_main.xml
git add app/src/main/res/layout/activity_customer.xml
git add app/src/main/res/layout/activity_account.xml
git add app/src/main/res/layout/activity_transaction.xml
git commit -m "ui: Add main activity layouts with proper UI components"

# Commit 11: List layouts
git add app/src/main/res/layout/activity_customer_list.xml
git add app/src/main/res/layout/activity_account_list.xml
git add app/src/main/res/layout/activity_settings.xml
git add app/src/main/res/layout/item_customer.xml
git add app/src/main/res/layout/item_account.xml
git commit -m "ui: Add list activity layouts and custom list item layouts"

# Commit 12: Resources and manifest
git add app/src/main/res/values/strings.xml
git add app/src/main/AndroidManifest.xml
git commit -m "config: Update strings.xml and AndroidManifest with all activities and permissions"

# Commit 13: Build configuration
git add app/build.gradle.kts
git add build.gradle.kts
git add settings.gradle.kts
git add gradle.properties
git commit -m "config: Add Gradle build configuration files"

# Commit 14: Gradle wrapper
git add gradle/
git add gradlew
git add gradlew.bat
git commit -m "config: Add Gradle wrapper files"

# Push to GitLab
git branch -M main
git push -u origin main

# ============================================
# OPTION 2: Quick Single Commit (If in a hurry)
# ============================================

# Uncomment these lines if you want to do it quickly:
# git add .
# git commit -m "feat: Complete Bank Management Android Application with all features"
# git branch -M main
# git push -u origin main

# ============================================
# Verify your commits
# ============================================
git log --oneline

# ============================================
# If you encounter "remote already exists" error:
# ============================================
# git remote remove origin
# git remote add origin https://gitlab.com/harerimanapacifique95/26937_midterm_bank.git

# ============================================
# If you need to pull first (repository has existing content):
# ============================================
# git pull origin main --allow-unrelated-histories
# git push -u origin main

# ============================================
# Check remote URL
# ============================================
git remote -v

# ============================================
# View commit history
# ============================================
git log --oneline --graph --all

echo "✅ All commits completed! Check your GitLab repository:"
echo "https://gitlab.com/harerimanapacifique95/26937_midterm_bank"
