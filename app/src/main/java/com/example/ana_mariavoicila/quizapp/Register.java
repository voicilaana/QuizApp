package com.example.ana_mariavoicila.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etUsername = (EditText) findViewById(R.id.etUserName);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }
}
