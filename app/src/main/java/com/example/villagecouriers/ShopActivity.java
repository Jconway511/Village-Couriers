package com.example.villagecouriers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
// created by Jason Conway
public class ShopActivity extends AppCompatActivity  {
    private EditText etItemName, etItemQuantity, etItemPrice;
    private Button btnCompleteOrder;
    private Button btnHome;
    private List<Order> orderList = new ArrayList<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);
        btnHome = findViewById(R.id.btnHome);


        etItemName = findViewById(R.id.etItemName);
        etItemQuantity = findViewById(R.id.etItemQuantity);
        etItemPrice = findViewById(R.id.etItemPrice);



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
                    long orderId = random.nextLong();
                    orderList.add(new Order(orderId,itemName, Integer.parseInt(itemQuantity), Double.parseDouble(itemPrice)));
                    writeOrdersToFile(orderList);
                    Toast.makeText(ShopActivity.this, "Order added successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ShopActivity.this, "Error adding order", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
