package com.example.villagecouriers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private ListView listView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        ListView listView = findViewById(R.id.listView);

        userRepository = new UserRepository(this);
        loadUsers();
    }
    private void loadUsers() {
        userRepository.open();
        List<User> users = userRepository.getAllUsers();
        userRepository.close();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);


    }
}
