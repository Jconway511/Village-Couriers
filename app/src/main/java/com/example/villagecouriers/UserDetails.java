package com.example.villagecouriers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class UserDetails  extends  AppCompatActivity {
    private UserRepository userRepository;
    private ListView listView;

    private void loadUser() {
        userRepository.open();
        User loggedInUser = userRepository.getLoggedInUser();
        userRepository.close();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, List.of(loggedInUser));
        listView.setAdapter(adapter);


    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);
        listView = findViewById(R.id.lvUserDetails);
        userRepository = new UserRepository(this);
        loadUser();
        Button btnDelete = findViewById(R.id.btnDeleteProfile);
        Button btnEdit = findViewById(R.id.btnEditProfile);
        Button btnLogOut = findViewById(R.id.btnLogOut);


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRepository.open();
                userRepository.logOut();
                userRepository.close();

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRepository.open();
                userRepository.deleteUser( userRepository.getLoggedInUser().getId());
                userRepository.close();

            }
        });

    }

}
