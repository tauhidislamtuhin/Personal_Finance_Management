package com.tittech.personalfinancemanagement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "sqlite_login", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(email TEXT primary key, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    //==============================================createTable
    public void createUserTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_TABLE_EXPENSE = "CREATE TABLE IF NOT EXISTS " + tableName + "expense (id INTEGER PRIMARY KEY AUTOINCREMENT, amount DOUBLE, reason TEXT, time TEXT)";
        String CREATE_TABLE_INCOME = "CREATE TABLE IF NOT EXISTS " + tableName + "income (id INTEGER PRIMARY KEY AUTOINCREMENT, amount DOUBLE, reason TEXT, time TEXT)";
        db.execSQL(CREATE_TABLE_EXPENSE);
        db.execSQL(CREATE_TABLE_INCOME);
    }

    //==============================================insertUser
    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //==============================================checkAlreadySingUp
    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //==============================================checkLogInEmailPassword
    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //==============================================addExpense
    public boolean addExpense(double amount, String reason, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conval = new ContentValues();
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", timeDateFormat());
        long returnExpense = db.insert(tableName + "_expense", null, conval);
        if (returnExpense > 0) return true;
        else return false;

    }

    //===============================================addIncome
    public boolean addIncome(double amount, String reason, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conval = new ContentValues();
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", timeDateFormat());
        long returnIncome = db.insert(tableName + "_income", null, conval);
        if (returnIncome > 0) return true;
        else return false;


    }

    //==============================================calculateTotalExpense
    public double calculateTotalExpense(String tableName) {
        double totalExpense = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + "_expense", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                double amount = cursor.getDouble(1);
                totalExpense = totalExpense + amount;
            }

        }
        return totalExpense;
    }

    //==============================================calculateTotalExpense
    public double calculateTotalIncome(String tableName) {
        double totalIncome = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + "_income", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                double amount = cursor.getDouble(1);
                totalIncome = totalIncome + amount;
            }

        }
        return totalIncome;
    }

    //==============================================showAllExpense
    public Cursor showAllExpense(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + "_expense", null);
        return cursor;
    }

    //==============================================showAllIncome
    public Cursor showAllIncome(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + "_income", null);
        return cursor;
    }


    //==============================================deleteByIdExpense
    public void deleteByIdExpense(String id, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName + "_expense WHERE id LIKE " + id);
    }

    //==============================================deleteByIdIncome
    public void deleteByIdIncome(String id, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName + "_income WHERE id LIKE " + id);
    }

    //==============================================updateExpense
    public boolean updateExpense(String id, double amount, String reason, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conval = new ContentValues();
        conval.put("id", id);
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", timeDateFormat());
        db.update(tableName + "_expense", conval, "id = ?", new String[]{id});
        return true;
    }

    //==============================================updateIncome
    public boolean updateIncome(String id, double amount, String reason, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues conval = new ContentValues();
        conval.put("id", id);
        conval.put("amount", amount);
        conval.put("reason", reason);
        conval.put("time", timeDateFormat());
        db.update(tableName + "_income", conval, "id = ?", new String[]{id});
        return true;
    }

    //==============================================timeDateFormat
    public String timeDateFormat() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTimeMillis));

        String dateTimeArray[] = formattedTime.split(" ");

        String date = dateTimeArray[0];
        String time = dateTimeArray[1];
        String amPm = "";

        String timeArray[] = time.split(":");
        String hour24 = timeArray[0];
        String minutes = timeArray[1];
        int hour12 = Integer.parseInt(hour24);

        if (hour12 > 12) {
            hour12 = hour12 - 12;
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        return "Date : " + date + " Time : " + hour12 + ":" + minutes + amPm;
    }
}
