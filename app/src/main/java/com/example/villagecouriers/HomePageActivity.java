package com.example.villagecouriers;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private Button btnShop;
    private Button btnOrders;
    private Button btnUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_page);

     btnShop = findViewById(R.id.btnShop);
     btnOrders = findViewById(R.id.btnOrders);
     btnUserDetails = findViewById(R.id.btnUserDetails);

        btnShop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(HomePageActivity.this, ShopActivity.class);
            startActivity(intent);
        }});

        btnOrders.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(HomePageActivity.this, OrderActivity.class);
            startActivity(intent);

        }});
        btnUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePageActivity.this, UserDetails.class);
                startActivity(intent);
            }});
    }
}
