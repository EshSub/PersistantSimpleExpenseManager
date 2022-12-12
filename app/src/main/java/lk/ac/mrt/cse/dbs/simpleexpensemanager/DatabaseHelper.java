package lk.ac.mrt.cse.dbs.simpleexpensemanager;


import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String NAME = "db";

    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String TABLE_TRANSACTIONS = "transactions";

    //common columns
//    private static final String PRIMARY_KEY = "id";
    public static final String ACCOUNT_NO = "accountNo";

    //accounts table columns
    public static final String BANK_NAME = "bankName";
    public static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String BALANCE = "balance";

    //transaction table columns
    public static final String TRANSACTION_ID = "id";
    public static final String TYPE = "expenseType";
    public static final String AMOUNT = "amount";
    public static final String DATE = "Date";

    private static final String CREATE_TABLE_ACCOUNTS ="CREATE TABLE " + TABLE_ACCOUNTS +"(" + ACCOUNT_NO +" VARCHAR(20) PRIMARY KEY," + BANK_NAME +" VARCHAR(20)," + ACCOUNT_HOLDER_NAME +" VARCHAR(100)," + BALANCE +" NUMERIC(15,2)" +")";
    private static final String CREATE_TABLE_TRANSACTIONS ="CREATE TABLE " + TABLE_TRANSACTIONS +"(" + TRANSACTION_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"  + ACCOUNT_NO +" VARCHAR(20) REFERENCES " + TABLE_ACCOUNTS + "(" + ACCOUNT_NO + "),"  + TYPE +" VARCHAR(20)," + AMOUNT +" NUMERIC(12,2)," + DATE +" TEXT" +")";
    public  DatabaseHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context,NAME,factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public List<Transaction> getTransactions() {
        System.out.println("ESHAN DB get accounts");
        List<Transaction> transactions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,     null);

        if (c.moveToFirst()) {
            do {
                System.out.println("ESHAN transaction date "+c.getString(c.getColumnIndex(DATE)));
                Transaction td = new Transaction();
                td.setAccountNo(c.getString(c.getColumnIndex(ACCOUNT_NO)));
                td.setDate(new Date(c.getString(c.getColumnIndex(DATE))));
                td.setAmount(c.getDouble(c.getColumnIndex(AMOUNT)));
                td.setExpenseType(ExpenseType.valueOf(c.getString(c.getColumnIndex(TYPE))));

                transactions.add(td);
            } while (c.moveToNext());
        }
        c.close();

        return transactions;
    }

    public long createTransaction(Transaction transaction){
        System.out.println("ESHAN db create transaction");
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, transaction.getAccountNo());
        values.put(DATE, transaction.getDate().toString());
        values.put(AMOUNT, transaction.getAmount());
        values.put(TYPE, transaction.getExpenseType().toString());

        long transaction_id = db.insert(TABLE_TRANSACTIONS, null, values);

        return transaction_id;
    }

    public long createAccount(Account account) {
        System.out.println("ESHAN DB create account "+account.getAccountNo());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, account.getAccountNo());
        values.put(ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(BALANCE, account.getBalance());
        values.put(BANK_NAME, account.getBankName());

        // insert row
        long account_id = db.insert(TABLE_ACCOUNTS, null, values);

        return account_id;
    }

    public void removeAccount(String accountNo){
        System.out.println("ESHAN DB delete account "+accountNo);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNTS, ACCOUNT_NO+" = ?", new String[] {accountNo});
    }

    public int updateAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BALANCE, account.getBalance());

        // updating row
        return db.update(TABLE_ACCOUNTS, values, ACCOUNT_NO + " = ?",
                new String[] { account.getAccountNo() });
    }

    public List<Account> getAccounts(){
        System.out.println("ESHAN DB get accounts");
        List<Account> accounts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,     null);

        if (c.moveToFirst()) {
            do {
                Account td = new Account();
                td.setAccountNo(c.getString(c.getColumnIndex(ACCOUNT_NO)));
                td.setBalance(c.getDouble(c.getColumnIndex(BALANCE)));
                td.setAccountHolderName(c.getString(c.getColumnIndex(ACCOUNT_HOLDER_NAME)));
                td.setBankName(c.getString(c.getColumnIndex(BANK_NAME)));

                accounts.add(td);
            } while (c.moveToNext());
        }
        c.close();
        return accounts;
    }

}
