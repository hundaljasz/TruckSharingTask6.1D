package com.example.trucksharingtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trucksharingtask.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String userName,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DB db = new DB(this);
        binding.signupBTN.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
        binding.loginBTN.setOnClickListener(view -> {
            userName = String.valueOf(binding.uname.getText());
            Password = String.valueOf(binding.password.getText());
            int result = db.getUser(userName, Password);
            if (result<=0) {
                Toast.makeText(this, "UserName or Password is Wrong!...", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("user", userName);
            intent.putExtra("UID", result);
            startActivity(intent);
            finish();
        });
    }
}