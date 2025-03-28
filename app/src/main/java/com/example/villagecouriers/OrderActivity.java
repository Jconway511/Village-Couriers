package com.example.villagecouriers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        itemOrderList = new ArrayList<>();
        itemOrderAdapter = new ItemOrderAdapter(itemOrderList);
        recyclerView.setAdapter(itemOrderAdapter);
    }

    private List<ItemOrder> readOrdersFromFile() {
        List<ItemOrder> itemOrders = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("orders.json")));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String itemName = jsonObject.getString("item_name");
                String itemQuantity = jsonObject.getString("item_quantity");
                String itemPrice = jsonObject.getString("item_price");
                String itemImage = jsonObject.getString("item_image");
                itemOrders.add(new ItemOrder(itemName, itemQuantity, itemPrice, itemImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemOrders;
    }
}
