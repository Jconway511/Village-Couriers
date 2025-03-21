package com.example.villagecouriers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public OrderRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Open the database connection
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }

    // Create a new user (Insert)
    public long createUser(String name, String email, String password, String usertype) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_USERTYPE, usertype);

        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }




    // Update user details
    public int updateUser(long id, String name, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);

        return database.update(DatabaseHelper.TABLE_NAME, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete user by ID
    public void deleteUser(long id) {
        database.delete(DatabaseHelper.TABLE_NAME,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }




}
