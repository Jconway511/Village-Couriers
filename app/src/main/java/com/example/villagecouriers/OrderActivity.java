package com.example.villagecouriers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemOrderAdapter itemOrderAdapter;
    private List<ItemOrder> itemOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Button btnHome = findViewById(R.id.btnHome);

        TextView itemNameTextView = findViewById(R.id.item_Name);
        TextView itemPriceTextView = findViewById(R.id.item_Price);
        TextView itemQuantityTextView = findViewById(R.id.item_quantity);

        try{
            Order order = readOrderFromFile();
            assert order != null;
            itemNameTextView.setText(order.getItemName());
            itemPriceTextView.setText(String.valueOf(order.getPrice()));
            itemQuantityTextView.setText(String.valueOf(order.getQuantity()));
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, HomePageActivity.class);
                startActivity(intent);
            }});
    }

    private Order readOrderFromFile() throws Exception {
        FileInputStream fis;
        try {
            fis = openFileInput("order.json");
        } catch (FileNotFoundException e) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        fis.close();

        JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
        long orderId = jsonObject.getLong("orderId");
        String itemName = jsonObject.getString("itemName");
        double price = jsonObject.getDouble("Price");
        int quantity = jsonObject.getInt("Quantity");

        return new Order(orderId,itemName, quantity, price);
    }
    private boolean isValidJSON(String jsonString) {
        try {
            new JSONObject(jsonString);
        } catch (JSONException ex) {
            try {
                new JSONArray(jsonString);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
