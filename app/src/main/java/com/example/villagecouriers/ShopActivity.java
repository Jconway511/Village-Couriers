package com.example.villagecouriers;

import android.content.ContentValues;
import android.content.Context;
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
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private ArrayList<String> itemList;
    private LinearLayout linearLayoutItems;
    private DatabaseHelper dbHelper;
    private UserRepository userRepository;

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
        userRepository = new UserRepository(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        btnAddItemToOrder = findViewById(R.id.btnAddItemToOrder);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);

        etItemName = findViewById(R.id.etItemName);
        etItemQuantity = findViewById(R.id.etItemQuantity);
        etItemPrice = findViewById(R.id.etItemPrice);
        dbHelper = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);

        btnAddItemToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newItem = new EditText(ShopActivity.this);
                newItem.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                newItem.setHint("Enter item");
                linearLayoutItems.addView(newItem);
            }
        });


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
                    List<ItemOrder> orders = readOrdersFromFile();
                    ItemOrder newOrder = new ItemOrder(itemName, itemQuantity, itemPrice, ""); // Assuming itemImage is not required
                    orders.add(newOrder);
                    writeOrdersToFile(orders);

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

    private List<ItemOrder> readOrdersFromFile() throws Exception {
        FileInputStream fis = openFileInput("orders.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        fis.close();

        JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
        List<ItemOrder> orders = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String itemName = jsonObject.getString("item_name");
            String itemQuantity = jsonObject.getString("item_quantity");
            String itemPrice = jsonObject.getString("item_price");
            String itemImage = jsonObject.getString("item_image");
            orders.add(new ItemOrder(itemName, itemQuantity, itemPrice, itemImage));
        }
        return orders;
    }

    private void writeOrdersToFile(List<ItemOrder> orders) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (ItemOrder order : orders) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("item_name", order.getItem_name());
            jsonObject.put("item_quantity", order.getItem_quantity());
            jsonObject.put("item_price", order.getItem_price());
            jsonObject.put("item_image", order.getItem_image());
            jsonArray.put(jsonObject);
        }

        FileOutputStream fos = openFileOutput("orders.json", Context.MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
    }

}
