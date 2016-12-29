package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;
import com.example.ana_mariavoicila.quizapp.Model.InputValidator;

public class Login extends AppCompatActivity {

    final DatabaseHandler dataBase = DatabaseHandler.getInstance(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registerIntent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(dataBase.validCredentials(etUsername.getText().toString(), etPassword.getText().toString())) {
                    Intent loginIntent = new Intent(Login.this, QuizQuestions.class);
                    Login.this.startActivity(loginIntent);

                } else {
                    invalidMessage("Username and password do not match.");
                }
            }
        });
    }

    void invalidMessage(String errorMsg) {
        CharSequence text = errorMsg;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
}
