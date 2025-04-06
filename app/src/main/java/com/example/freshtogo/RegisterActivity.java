package com.example.freshtogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput;
    private Button registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.register_username);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.register_login_link);

        registerButton.setOnClickListener(v -> handleRegister());

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        File filesDir = getFilesDir();
        int userId = 1;

        while (new File(filesDir, "user" + userId + ".txt").exists()) {
            userId++;
        }

        File newUserFile = new File(filesDir, "user" + userId + ".txt");

        try (FileWriter writer = new FileWriter(newUserFile)) {
            writer.write("USER_ID: " + userId + "\n");
            writer.write("USERNAME: " + username + "\n");
            writer.write("EMAIL: " + email + "\n");
            writer.write("PASSWORD: " + password + "\n");
            writer.write("CART:\n");
            writer.write("ORDER_HISTORY:\n");
            writer.write("CARDS:\n");
        } catch (IOException e) {
            Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
        SessionManager.setCurrentUser(this, "user" + userId);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
