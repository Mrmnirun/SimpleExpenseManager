package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class EmbeddedTransactionDAO implements TransactionDAO {
    SQLiteDatabase db;

    public EmbeddedTransactionDAO(SQLiteDatabase db){
        this.db =db;
    }


    @Override
    public void logTransaction(Date date_, String accountNo, ExpenseType expenseType_, double amount_){
        String InsertionForAccTransaction = "INSERT INTO Account_Transaction (accountNo,expenseType,amount,date) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(InsertionForAccTransaction);
        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType_ == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount_);
        statement.bindLong(4,date_.getTime());
        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        String SelectionForAccountTransaction = "SELECT * FROM Account_Transaction";
        Cursor cursor = db.rawQuery(SelectionForAccountTransaction, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction=new Transaction(
                            new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            cursor.getDouble(cursor.getColumnIndex("amount")));
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionDetail = new ArrayList<>();
        String SelectionForAccountTransactionDetail = "SELECT * FROM Account_Transaction LIMIT"+limit;
        Cursor cursor = db.rawQuery(SelectionForAccountTransactionDetail, null);
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction=new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("accountNo")),
                        (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        cursor.getDouble(cursor.getColumnIndex("amount")));
                transactionDetail.add(transaction);
            } while (cursor.moveToNext());
        }
        return  transactionDetail;
    }
}
