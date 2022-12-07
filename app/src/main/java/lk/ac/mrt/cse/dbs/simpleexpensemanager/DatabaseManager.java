package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


public class DatabaseManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context, null);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }




}
