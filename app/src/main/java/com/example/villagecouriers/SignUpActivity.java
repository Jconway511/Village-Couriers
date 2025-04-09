package com.example.villagecouriers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
// created by Jason Conway
public class SignUpActivity extends AppCompatActivity {
    private Spinner spinnerUserType;
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;

    private void saveLoggedInUser(String name, String email, String userType) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("userType", userType);
        editor.apply();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        spinnerUserType = findViewById(R.id.spinnerUserType);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerUserType.setAdapter(adapter);




    btnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String userType = spinnerUserType.getSelectedItem().toString();

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

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                etConfirmPassword.requestFocus();
                return;
            }
            try{
                List<User> users = readUsersFromFile();

                for (User user : users) {
                    if (user.getEmail().equalsIgnoreCase(email)) {
                        etEmail.setError("Email already exists");
                        etEmail.requestFocus();
                        return;
                    }
                }
                int newUserId = users.size() + 1;
                User newUser = new User(newUserId, name, email, password, userType);
                users.add(newUser);
                writeUsersToFile(users);

                saveLoggedInUser(newUser.getName(), newUser.getEmail(), newUser.getUserType());
                Toast.makeText(SignUpActivity.this, "Selected User Type: " + userType, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        });
    }
    private List<User> readUsersFromFile() throws Exception {
        FileInputStream fis;
        try {
            fis = openFileInput("users.json");
        } catch (FileNotFoundException e) {
            // Create the file if it doesn't exist
            FileOutputStream fos = openFileOutput("users.json", Context.MODE_PRIVATE);
            fos.write("[]".getBytes()); // Initialize with an empty JSON array
            fos.close();
            fis = openFileInput("users.json");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        fis.close();

        JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            String userType = jsonObject.getString("userType");
            users.add(new User(id, name, email, password, userType));
        }
        return users;
    }

    private void writeUsersToFile(List<User> newUsers) throws Exception {

        List<User> existingUsers = readUsersFromFile();
        existingUsers.addAll(newUsers);
        JSONArray jsonArray = new JSONArray();
        for (User user : existingUsers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getId());
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("userType", user.getUserType());
            jsonArray.put(jsonObject);
        }

        FileOutputStream fos = openFileOutput("users.json", Context.MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
    }
}


