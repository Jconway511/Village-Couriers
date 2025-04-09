package com.example.villagecouriers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
// created by Jason Conway
public class MainActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;


    private void saveLoggedInUser(String name, String email, String userType) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("userType", userType);
        editor.apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogIn = findViewById(R.id.btnLogIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (name.isEmpty()) {
                    etName.setError("Name is required");
                    etName.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                    return;
                }
                if (validateUser(email, password)) {
                    User loggedInUser = new User(0, name, email, password, "user"); // Replace with actual user details
                    saveLoggedInUser(loggedInUser.getName(), loggedInUser.getEmail(), loggedInUser.getUserType());
                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }

            }});

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

                }});
        }

    private List<User> readUsersFromFile() throws Exception {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("users.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        inputStream.close();

        JSONObject rootObject = new JSONObject(jsonBuilder.toString());
        JSONArray jsonArray = rootObject.getJSONArray("users");
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int userId = jsonObject.getInt("userId");
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            String userType = jsonObject.getString("userType");

            System.out.println("User: " + name + ", Email: " + email + ", Password: " + password + ", UserType: " + userType);

            users.add(new User(userId ,name, email, password, userType));
        }
        return users;
    }

    private boolean validateUser(String email, String password) {
        try {
            List<User> users = readUsersFromFile();
            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}