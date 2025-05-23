package com.example.villagecouriers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
// created by Jason Conway
public class UserRepository {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
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

    // Read all users (Select)
    public <User> List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_EMAIL},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                User user = (User) cursorToUser(cursor);
                users.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return users;
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

    // Get the logged in user
    public User getLoggedInUser() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_EMAIL},
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursorToUser(cursor);
    }
    public void logOut() {

    }

    // Helper method to convert cursor to User object
    @SuppressLint("Range")
    private User cursorToUser(Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERTYPE))
        );
    }
}
