package com.example.villagecouriers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class UserDetails  extends  AppCompatActivity {
    private RecyclerView recyclerViewUserDetails;
    private UserDetailsAdapter userDetailsAdapter;
    private List<User> userList;
    private UserRepository userRepository;
    private ListView lvUserDetails;

    private void displayUserDetails(User user) {
        List<String> userDetails = new ArrayList<>();
        userDetails.add("Name: " + user.getName());
        userDetails.add("Email: " + user.getEmail());
        userDetails.add("User Type: " + user.getUserType());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDetails);
        lvUserDetails.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);


        recyclerViewUserDetails = findViewById(R.id.lvUserDetails);
        recyclerViewUserDetails.setLayoutManager(new LinearLayoutManager(this));


        userRepository.open();
        userList = readUsersFromFile();
        User loggedInUser = userRepository.getLoggedInUser();

        userDetailsAdapter = new UserDetailsAdapter(userList);
        recyclerViewUserDetails.setAdapter(userDetailsAdapter);

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
    private List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("users.json")));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String email = jsonObject.getString("email");
                String password = jsonObject.getString("password");
                String userType = jsonObject.getString("userType");
                users.add(new User(id, name, email, password, userType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


}
