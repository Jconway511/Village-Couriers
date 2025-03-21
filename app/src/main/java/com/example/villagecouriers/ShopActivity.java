package com.example.villagecouriers;


import android.content.ContentValues;
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

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity  {
    private EditText etItem;
    private Button btnAddItemToOrder;
    private Button btnCompleteOrder;
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private ArrayList<String> itemList;
    private LinearLayout linearLayoutItems;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        etItem = findViewById(R.id.etItem1);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        btnAddItemToOrder = findViewById(R.id.btnAddItemToOrder);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);
        linearLayoutItems = findViewById(R.id.linearLayoutItems);
        dbHelper = new DatabaseHelper(this);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);

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
                String item = etItem.getText().toString().trim();
                if (!item.isEmpty()) {
                    itemList.add(item);
                    itemAdapter.notifyDataSetChanged();
                    etItem.setText("");
                    insertOrder(item);
                } else {
                    Toast.makeText(ShopActivity.this, "Please enter an item", Toast.LENGTH_SHORT).show();
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

}
