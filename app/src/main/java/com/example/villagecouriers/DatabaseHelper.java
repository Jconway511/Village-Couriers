package com.example.villagecouriers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "orders.db";

    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_ITEM = "order_item";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERTYPE = "usertype";

    // SQL query to create the table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ITEM + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL," +
                    COLUMN_USERTYPE + " TEXT NOT NULL);";

    private static final String TABLE_ORDERS_CREATE =
            "CREATE TABLE " + TABLE_ORDERS + "(" +
            COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ORDER_ITEM + "TEXT NOT NULL, " +
                    COLUMN_ORDER_USER_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_ORDERS_CREATE);
    }
    public void generateSampleUsers() {
        insertUser("John Doe", "john.doe@example.com", "password123", "Customer");
        insertUser("Jane Smith", "jane.smith@example.com", "password456", "Courier");
        insertUser("Alice Johnson", "alice.johnson@example.com", "password789", "Customer");
        insertUser("Bob Brown", "bob.brown@example.com", "password012", "Courier");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public void insertUser(String name, String email, String password, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USERTYPE, userType);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Method to get all users
    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_NAME, COLUMN_EMAIL, COLUMN_USERTYPE}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String user = "Name: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)) +
                        ", Email: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)) +
                        ", UserType: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERTYPE));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    // Method to insert an order
    public void insertOrder(String item, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ITEM, item);
        values.put(COLUMN_ORDER_USER_ID, userId);
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    // Method to get all orders
    public ArrayList<String> getAllOrders() {
        ArrayList<String> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, new String[]{COLUMN_ORDER_ITEM, COLUMN_ORDER_USER_ID}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String order = "Item: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM)) +
                        ", User ID: " + cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }
}

