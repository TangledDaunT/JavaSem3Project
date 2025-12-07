# 🚀 BankBridge - Quick Start Guide

## ⚡ Get Started in 5 Minutes!

---

## 📋 Prerequisites Checklist

Before you begin, make sure you have:

- [ ] **Java JDK 8 or higher** installed
- [ ] **SQLite JDBC Driver** downloaded
- [ ] **Terminal/Command Prompt** access

---

## 🎯 Step-by-Step Setup

### Step 1: Verify Java Installation

Open terminal and run:
```bash
java -version
```

✅ You should see Java version 8 or higher.

❌ If not installed, download from: https://www.oracle.com/java/technologies/downloads/

---

### Step 2: Download SQLite JDBC Driver

1. Go to: **https://github.com/xerial/sqlite-jdbc/releases**
2. Download: `sqlite-jdbc-3.46.0.0.jar` (or latest version)
3. Place it in: `BankBridge/lib/` folder
4. Rename to: `sqlite-jdbc.jar`

---

### Step 3: Compile the Application

#### Linux/macOS:
```bash
cd BankBridge
./compile.sh
```

#### Windows:
```cmd
cd BankBridge
compile.bat
```

⏳ This will take 10-30 seconds...

✅ You should see: **"Compilation successful!"**

---

### Step 4: Run the Application

#### Linux/macOS:
```bash
./run.sh
```

#### Windows:
```cmd
run.bat
```

🎉 **The login window should appear!**

---

## 🔐 Login Information

### Admin Account:
- **Username:** `admin`
- **Password:** `admin123`
- **Access:** Full admin panel + all features

### Sample User Account:
- **Username:** `john_doe`
- **Password:** `password123`
- **Access:** Standard user features

### Create Your Own:
Click **"Register"** button on login screen!

---

## 🎮 What to Do First?

### As a User:
1. ✅ **Login** with credentials
2. ✅ **Create Account** → Choose Savings or Current
3. ✅ **Make Deposit** → Add money to your account
4. ✅ **Try Withdrawal** → Take money out
5. ✅ **Transfer Funds** → Between your accounts
6. ✅ **View History** → See all transactions

### As an Admin:
1. ✅ **Login as admin**
2. ✅ **Open Admin Panel**
3. ✅ **View Statistics** → System overview
4. ✅ **Monitor Users** → All registered users
5. ✅ **Check Accounts** → All accounts in system
6. ✅ **Audit Logs** → Background monitoring

---

## 💡 Common Issues & Quick Fixes

### Issue: "ClassNotFoundException: org.sqlite.JDBC"
**Fix:** Make sure `sqlite-jdbc.jar` is in the `lib/` folder

### Issue: "Could not find or load main class Main"
**Fix:** Run `compile.sh` or `compile.bat` first

### Issue: "Database connection failed"
**Fix:** 
```bash
mkdir database
chmod 755 database
```

### Issue: GUI doesn't show
**Fix:** Make sure you're running a desktop Java version (not headless)

---

## 📚 Next Steps

After getting it running:

1. **Read README.md** → Full project documentation
2. **Check SETUP_GUIDE.md** → Detailed setup instructions
3. **View PROJECT_INFO.md** → Technical details
4. **Explore the code** → Learn from implementation

---

## 🎯 Testing Checklist

Try these features to test the system:

- [ ] Register a new user
- [ ] Create a savings account
- [ ] Make a deposit ($1000)
- [ ] Make a withdrawal ($500)
- [ ] Create a second account
- [ ] Transfer money between accounts
- [ ] View transaction history
- [ ] Login as admin
- [ ] View system statistics

---

## 📞 Need Help?

### Documentation Files:
- `README.md` → Main documentation
- `SETUP_GUIDE.md` → Detailed setup
- `PROJECT_INFO.md` → Technical details

### Check These:
1. Java version: `java -version`
2. Compilation status: Look for `out/Main.class`
3. JDBC driver: Check `lib/sqlite-jdbc.jar` exists
4. Database folder: Should auto-create `database/bank.db`

---

## ✨ Features You Can Try

### 🏦 Banking Operations:
- ✅ Create Savings Account (4% interest, $500 minimum)
- ✅ Create Current Account (overdraft facility)
- ✅ Deposit money
- ✅ Withdraw money (with balance check)
- ✅ Transfer between accounts

### 🔐 Security:
- ✅ Encrypted password (SHA-256)
- ✅ Secure login
- ✅ Session management

### 👨‍💼 Admin Features:
- ✅ View all users
- ✅ Monitor all accounts
- ✅ System statistics
- ✅ Transaction audit logs

### 🧵 Background Features:
- ✅ Async transaction processing
- ✅ Auto-audit every 60 seconds
- ✅ Suspicious transaction detection

---

## 🎓 Educational Value

This project demonstrates:

- ✅ **OOP Concepts** → All major concepts
- ✅ **JDBC** → Database operations
- ✅ **Multithreading** → Concurrent processing
- ✅ **GUI Development** → Professional Swing UI
- ✅ **Design Patterns** → 7+ patterns
- ✅ **Security** → Encryption & validation
- ✅ **Architecture** → Clean layered design

---

## 🚀 Performance Notes

- **Startup Time:** 2-5 seconds
- **Transaction Processing:** Asynchronous (non-blocking)
- **Database:** Lightweight SQLite (no server needed)
- **Memory Usage:** ~50-100 MB
- **Thread Count:** 3 (Main + TransactionEngine + AuditThread)

---

## 📊 Project Stats

- **Java Files:** 28
- **Lines of Code:** 3,500+
- **Packages:** 5
- **Classes:** 26
- **Interfaces:** 2
- **Design Patterns:** 7+

---

## 🎉 Success Indicators

You'll know it's working when you see:

```
============================================================
       🏦 BANKBRIDGE - Banking Management System
============================================================

[1/4] Initializing database connection...
✓ Database connection successful

[2/4] Checking for default admin user...
✓ Admin user check complete

[3/4] Creating sample test users...
✓ Sample users created

[4/4] Launching user interface...
✓ GUI launched successfully

============================================================

✅ BankBridge is now running!

Default Admin Credentials:
  Username: admin
  Password: admin123

Sample User Credentials:
  Username: john_doe
  Password: password123

============================================================
```

And the **Login Window** appears! 🎊

---

## 🌟 Pro Tips

1. **First Time?** → Try admin account to see all features
2. **Learning?** → Create multiple accounts and test transfers
3. **Testing?** → Check the audit logs in terminal
4. **Developing?** → Modify and recompile with compile script
5. **Presenting?** → Start with admin panel statistics

---

## 📅 What's Next?

After running successfully:

1. ✅ Test all features thoroughly
2. ✅ Read the source code
3. ✅ Understand OOP implementation
4. ✅ Study the database schema
5. ✅ Learn the design patterns used
6. ✅ Customize for your needs

---

## 🏆 You're All Set!

**Congratulations!** You now have a fully functional banking system running locally.

Explore, learn, and enjoy! 🎉

---

*Happy Banking! 🏦*

**BankBridge Team**
*Shreyansh Misra & Shivam*
