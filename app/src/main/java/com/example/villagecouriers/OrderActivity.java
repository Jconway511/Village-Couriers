package com.example.villagecouriers;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        TextView tvItemName = findViewById(R.id.item_name);
        TextView tvQuantity = findViewById(R.id.item_quantity);
        TextView tvPrice = findViewById(R.id.item_price);
        Button btnHome = findViewById(R.id.btnHome);



        try{
            List<Order> orderList = readOrdersFromFile();
            if (!orderList.isEmpty()) {
                Order order = orderList.get(0); // Display the first order for simplicity
                tvItemName.setText(order.getItemName());
                tvQuantity.setText(String.valueOf(order.getQuantity()));
                tvPrice.setText(String.valueOf(order.getPrice()));
            } else {
                Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
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

    private List<Order> readOrdersFromFile() throws Exception {

        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("orders.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        inputStream.close();

        JSONArray jsonArray= new JSONArray(jsonBuilder.toString());
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);;
        long orderId = jsonObject.getLong("orderId");
        String itemName = jsonObject.getString("itemName");
        double price = jsonObject.getDouble("price");
        int quantity = jsonObject.getInt("quantity");

        orders.add(new Order(orderId,itemName, quantity, price));
    }
        for (Order order : orders) {
            System.out.println("Order: " + order.getItemName() + ", Quantity: " + order.getQuantity() + ", Price: " + order.getPrice());
        }
    return orders;
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
