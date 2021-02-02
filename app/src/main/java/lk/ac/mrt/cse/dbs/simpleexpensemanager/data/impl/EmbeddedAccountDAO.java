package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class EmbeddedAccountDAO implements AccountDAO {
    SQLiteDatabase db;

    public EmbeddedAccountDAO(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<>();
        String SelectionQueryForAccountNumber = "SELECT accountNo FROM Account";
        Cursor cursor = db.rawQuery(SelectionQueryForAccountNumber, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String string=cursor.getString(cursor.getColumnIndex("accountNo"));
                    accountNumbers.add(string);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountdetails = new ArrayList<>();
        String SelectionQueryForAccountDetails = "SELECT * FROM Account";
        Cursor cursor = db.rawQuery(SelectionQueryForAccountDetails, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Account account=new Account(
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            cursor.getString(cursor.getColumnIndex("bankName")),
                            cursor.getString(cursor.getColumnIndex("accountHolderName")),
                            cursor.getDouble(cursor.getColumnIndex("balance")));
                    accountdetails.add(account);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return accountdetails;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String AccountSelectionFromAccNo = "SELECT * FROM Account where accountNo = "+accountNo;
        Cursor cursor = db.rawQuery(AccountSelectionFromAccNo, null);
        if (cursor != null)
            cursor.moveToFirst();
        Account account = new Account(
                cursor.getString(cursor.getColumnIndex("accountNo")),
                cursor.getString(cursor.getColumnIndex("bankName")),
                cursor.getString(cursor.getColumnIndex("accountHolderName")),
                cursor.getDouble(cursor.getColumnIndex("balance")));
        return account;
    }

    @Override
    public void addAccount(Account account) {
        String InsertionForAccount = "INSERT INTO Account (accountNo,bankName,accountHolderName,balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(InsertionForAccount);
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());
        statement.executeInsert();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String RemovalOfAccount = "DELETE FROM Account WHERE accountNo = ?";
        SQLiteStatement statement = db.compileStatement(RemovalOfAccount);
        statement.bindString(1,accountNo);
        statement.executeUpdateDelete();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void updateBalance(String accountNo, ExpenseType expense_Type, double _amount) throws InvalidAccountException {
        String BalanceUpdate = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = db.compileStatement(BalanceUpdate);
        if(expense_Type == ExpenseType.EXPENSE){
            statement.bindDouble(1,-_amount);
        }else{
            statement.bindDouble(1,_amount);
        }
        statement.executeUpdateDelete();
    }
}

