package com.example.villagecouriers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView userName;
    private TextView userEmail;
    private TextView userType;
    private List<User> userList;
    private UserRepository userRepository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        userName = findViewById(R.id.user_Name);
        userEmail = findViewById(R.id.user_Email);
        userType = findViewById(R.id.user_Type);


        userRepository = new UserRepository(this);
        userRepository.open();
        User loggedInUser = userRepository.getLoggedInUser();

        if (loggedInUser != null) {
            userName.setText("User name: "+loggedInUser.getName());
            userEmail.setText("Email: "+loggedInUser.getEmail());
            userType.setText("User Type: "+loggedInUser.getUserType());
        }


        Button btnDelete = findViewById(R.id.btnDeleteProfile);
        Button btnEdit = findViewById(R.id.btnEditProfile);
        Button btnLogOut = findViewById(R.id.btnLogOut);
        Button btnHome = findViewById(R.id.btnHome);

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
                btnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserDetails.this, HomePageActivity.class);
                        startActivity(intent);

                    }});

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
    private void displayUserDetails(User user) {
        List<String> userDetails = new ArrayList<>();
        userDetails.add("Name: " + user.getName());
        userDetails.add("Email: " + user.getEmail());
        userDetails.add("User Type: " + user.getUserType());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDetails);
    }
}
