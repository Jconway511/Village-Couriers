package com.example.villagecouriers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity  {
    private static final String Orders_File = "orders.json";
    private Gson gson = new Gson();
    private EditText etItemName, etItemQuantity, etItemPrice;
    private Button btnAddItemToOrder;
    private Button btnCompleteOrder;
    private Button btnHome;
    private List<Order> orderList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    private List<Order> readOrders(){
        try (FileReader reader = new FileReader(Orders_File)){
            return gson.fromJson(reader, new TypeToken<List<Order>>(){}.getType());
        } catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void addOrder(Order newOrder) {
        List<Order> orders = readOrders();
        orders.add(newOrder);
        writeOrders(orders);
    }
    private void writeOrders(List<Order> orders) {
        try (FileWriter writer = new FileWriter(Orders_File)) {
            gson.toJson(orders, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        btnAddItemToOrder = findViewById(R.id.btnAddItemToOrder);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);
        btnHome = findViewById(R.id.btnHome);


        etItemName = findViewById(R.id.etItemName);
        etItemQuantity = findViewById(R.id.etItemQuantity);
        etItemPrice = findViewById(R.id.etItemPrice);
        dbHelper = new DatabaseHelper(this);


        btnAddItemToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etItemName.getText().toString().trim();
                int quantity = Integer.parseInt(etItemQuantity.getText().toString().trim());
                double price = Double.parseDouble(etItemPrice.getText().toString().trim());
                long orderId = System.currentTimeMillis(); // Generate a unique order ID

                Order order = new Order(orderId ,itemName, quantity, price);
                orderList.add(order);
                Toast.makeText(ShopActivity.this, "Item added to order", Toast.LENGTH_SHORT).show();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, HomePageActivity.class);
                startActivity(intent);
            }});
        btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etItemName.getText().toString().trim();
                String itemQuantity = etItemQuantity.getText().toString().trim();
                String itemPrice = etItemPrice.getText().toString().trim();

                if (itemName.isEmpty()) {
                    etItemName.setError("Item name is required");
                    etItemName.requestFocus();
                    return;
                }

                if (itemQuantity.isEmpty()) {
                    etItemQuantity.setError("Item quantity is required");
                    etItemQuantity.requestFocus();
                    return;
                }

                if (itemPrice.isEmpty()) {
                    etItemPrice.setError("Item price is required");
                    etItemPrice.requestFocus();
                    return;
                }
                try {
                    writeOrdersToFile(orderList);
                    Toast.makeText(ShopActivity.this, "Order added successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ShopActivity.this, "Error adding order", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ArrayList<String> orders = dbHelper.getAllOrders();
        for (String order : orders) {
            // Display the orders as needed
            System.out.println(order);
        }
    }
    private void insertOrder(String item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM, item);
        long newRowId = db.insert(DatabaseHelper.TABLE_ORDERS, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Order added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding order", Toast.LENGTH_SHORT).show();
        }
    }
    private void writeOrdersToFile(List<Order> orders) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (Order order : orders) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("item_name", order.getItemName());
            jsonObject.put("item_quantity", order.getQuantity());
            jsonObject.put("item_price", order.getPrice());
            jsonArray.put(jsonObject);
        }

        FileOutputStream fos = openFileOutput("orders.json", Context.MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
    }

}
