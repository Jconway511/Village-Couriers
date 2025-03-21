package com.example.villagecouriers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewItems;
    private OrderAdapter orderAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        dbHelper = new DatabaseHelper(getApplicationContext());

        ArrayList<String> orders = dbHelper.getAllOrders();
        orderAdapter = new OrderAdapter(orders);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(orderAdapter);
    }
}
