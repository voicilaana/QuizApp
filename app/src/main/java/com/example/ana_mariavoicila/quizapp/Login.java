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

    private EditText etUsername;
    private EditText etPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initParams();
        initListeners();
    }

    private void initParams() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
    }

    private void initListeners() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), Register.class);
                registerIntent.putExtra("caller", "Login");
                startActivityForResult(registerIntent, 2);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(DatabaseHandler.getInstance(getApplicationContext()).validCredentials(etUsername.getText().toString(), etPassword.getText().toString())) {
                    Intent quizQuestionsIntent = new Intent(getApplicationContext(), QuizQuestions.class);
                    quizQuestionsIntent.putExtra("caller", "Login");
                    startActivityForResult(quizQuestionsIntent, 1);
                    // TODO: Should change quizQuestionsIntent to modeSelectionIntent (single/multi player)
                    // TODO: Multi-player should login/register/play-anon
                } else {
                    invalidMessage("Username and password do not match.");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            etUsername.setText(data.getStringExtra("USERNAME"));
            etPassword.setText(data.getStringExtra("PASSWORD"));
            buttonRegister.setEnabled(false);
        } else if (requestCode == 1) {
            DatabaseHandler.getInstance(getApplicationContext()).logoutUser(etUsername.getText().toString());
            etUsername.setText("");
            etPassword.setText("");
        }
    }

    private void invalidMessage(String errorMsg) {
        toast.setText(errorMsg);
        toast.show();
    }
}
