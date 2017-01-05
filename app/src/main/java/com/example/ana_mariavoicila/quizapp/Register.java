package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;
import com.example.ana_mariavoicila.quizapp.Model.InputValidator;
import com.example.ana_mariavoicila.quizapp.Model.User;

public class Register extends AppCompatActivity {

    private EditText etPassword;
    private EditText etPasswordConfirmed;
    private EditText etUsername;
    private Button buttonRegister;

    private Toast toast;
    private String message;
    private DatabaseHandler dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initParams();
        initListeners();
    }

    private void initListeners() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    User newUser = new User(etUsername.getText().toString(), etPassword.getText().toString());
                    dataBase.addUser(newUser);

                    Intent intent = new Intent();
                    intent.putExtra("USERNAME", newUser.getUserName());
                    intent.putExtra("PASSWORD", newUser.getPassWord());
                    setResult(2, intent);
                    finish();
                }

                showMessage(message);
            }
        });
    }

    private void initParams() {
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        dataBase = DatabaseHandler.getInstance(getApplicationContext());
        etPasswordConfirmed = (EditText) findViewById(R.id.etPasswordConfirm);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUserName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private boolean validate() {

        message = "Success!";
        InputValidator inputValidatorHelper = new InputValidator();
        boolean patternKey = true;

        if (inputValidatorHelper.isNullOrEmpty(etUsername.getText().toString())){
            message = "Username cannot be empty.";
            return false;
        }

        if (!(dataBase.isValidUsername(etUsername.getText().toString()))) message = "Username taken.";

        if (inputValidatorHelper.isValidPassword (etPassword.getText().toString(), patternKey)) {
            if (patternKey) {
                message = "Password should contain a special character.";
            } else {
                message = "Password should contain only letters and numbers.";
            }
            return false;
        }

        if (inputValidatorHelper.isValidPassword(etPasswordConfirmed.getText().toString(), patternKey)) {
            if (patternKey) {
                message = "Password should contain a special character.";
            } else {
                message = "Password should contain only letters and numbers.";
            }
            return false;
        }

        if(!inputValidatorHelper.identicStrings(etPassword.getText().toString(), etPasswordConfirmed.getText().toString())) {
            message = "Your passwords do not match.";
            return false;
        }

        return true;
    }

    private void showMessage(String message) {
        toast.setText(message);
        toast.show();
    }

}
