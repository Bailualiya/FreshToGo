package com.example.freshtogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.login_register_link);

        loginButton.setOnClickListener(v -> handleLogin());

        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void handleLogin() {
        String enteredUsername = usernameInput.getText().toString().trim();
        String enteredPassword = passwordInput.getText().toString().trim();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        File filesDir = getFilesDir();
        File[] userFiles = filesDir.listFiles();

        if (userFiles != null) {
            for (File file : userFiles) {
                if (file.getName().startsWith("user") && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        String username = null;
                        String password = null;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("USERNAME: ")) {
                                username = line.replace("USERNAME: ", "").trim();
                            } else if (line.startsWith("PASSWORD: ")) {
                                password = line.replace("PASSWORD: ", "").trim();
                            }
                        }

                        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                            SessionManager.setCurrentUser(this, file.getName().replace(".txt", ""));
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
}
