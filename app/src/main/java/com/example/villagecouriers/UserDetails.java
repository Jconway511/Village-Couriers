package com.example.villagecouriers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// created by Jason Conway
public class UserDetails  extends  AppCompatActivity {
    private TextView userName;
    private TextView userEmail;
    private TextView userType;

    private User getLoggedInUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String userType = sharedPreferences.getString("userType", null);

        if (name != null && email != null && userType != null) {
            return new User(0, name, email, "", userType); // Password is not stored for security
        }
        return null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        userName = findViewById(R.id.user_Name);
        userEmail = findViewById(R.id.user_Email);
        userType = findViewById(R.id.user_Type);



        User loggedInUser = getLoggedInUser();
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
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all stored user data
                editor.apply();
                Intent intent = new Intent(UserDetails.this, MainActivity.class);
                startActivity(intent);
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
                deleteUser(loggedInUser);
                Intent intent = new Intent(UserDetails.this, MainActivity.class);
                startActivity(intent);
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


    private void deleteUser(User user) {
        try {
            List<User> users = readUsersFromFile();
            users.removeIf(u -> u.getEmail().equals(user.getEmail()));
            JSONArray jsonArray = new JSONArray();
            for (User u : users) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", u.getId());
                jsonObject.put("name", u.getName());
                jsonObject.put("email", u.getEmail());
                jsonObject.put("password", u.getPassword());
                jsonObject.put("userType", u.getUserType());
                jsonArray.put(jsonObject);
            }
            FileOutputStream fos = openFileOutput("users.json", MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
