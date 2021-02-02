package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.EmbeddedAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.EmbeddedTransactionDAO;

public class EmbeddedDemoExpenseManager extends ExpenseManager {
    Context ctx;
    public EmbeddedDemoExpenseManager(Context ctx)  {
        this.ctx =ctx;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() throws ExpenseManagerException {
        SQLiteDatabase db = ctx.openOrCreateDatabase("180431V", ctx.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "accountNo VARCHAR PRIMARY KEY," +
                "bankName VARCHAR," +
                "accountHolderName VARCHAR," +
                "balance REAL" +
                " );");

        db.execSQL("CREATE TABLE IF NOT EXISTS Account_Transaction(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "accountNo VARCHAR," +
                "expenseType INT," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (accountNo) REFERENCES Account(accountNo)" +
                ");");


        EmbeddedAccountDAO accountDAO = new EmbeddedAccountDAO(db);
        setAccountsDAO(accountDAO);
        setTransactionsDAO(new EmbeddedTransactionDAO(db));

    }
}
