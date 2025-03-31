package com.example.freshtogo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerUsernameInput, registerPasswordInput;
    private Button registerButton;
    private AppDatabase db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsernameInput = findViewById(R.id.registerUsernameInput);
        registerPasswordInput = findViewById(R.id.registerPasswordInput);
        registerButton = findViewById(R.id.registerButton);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "fresh_to_go_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        userDao = db.userDao();

        registerButton.setOnClickListener(v -> {
            String username = registerUsernameInput.getText().toString().trim();
            String password = registerPasswordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userDao.getUserByUsername(username) != null) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.username = username;
            newUser.password = password;
            userDao.insertUser(newUser);

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}