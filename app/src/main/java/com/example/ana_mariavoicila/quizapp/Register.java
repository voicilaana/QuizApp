package com.example.ana_mariavoicila.quizapp;

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

    EditText etPassword;
    EditText etPasswordConfirmed;
    EditText etUsername;
    EditText etName;
    Button buttonRegister;

    String errorMsg = new String();
    final DatabaseHandler dataBase = DatabaseHandler.getInstance(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etPasswordConfirmed = (EditText) findViewById(R.id.etPasswordConfirm);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUserName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    User newUser = new User(etUsername.getText().toString(), etPassword.getText().toString());
                    dataBase.addUser(newUser);
                } else {
                    CharSequence text = errorMsg;
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });

    }

    private boolean validate() {

        errorMsg = "Success!";
        InputValidator inputValidatorHelper = new InputValidator();
        boolean patternKey = true;

        if (inputValidatorHelper.isNullOrEmpty(etUsername.getText().toString())){
            errorMsg = "Username cannot be empty.";
            return false;
        }

        if (!(dataBase.isValidUsername(etUsername.getText().toString()))) errorMsg = "Username taken.";

        if (inputValidatorHelper.isNullOrEmpty (etName.getText().toString())){
            errorMsg = "First name cannot be empty.";
            return false;
        }

        if (inputValidatorHelper.isValidPassword (etPassword.getText().toString(),patternKey)) {
            if (patternKey) {
                errorMsg = "Password should contain a special character.";
            } else {
                errorMsg = "Password should contain only letters and numbers.";
            }
            return false;
        }

        if (inputValidatorHelper.isValidPassword(etPasswordConfirmed.getText().toString(),patternKey)) {
            if (patternKey) {
                errorMsg = "Password should contain a special character.";
            } else {
                errorMsg = "Password should contain only letters and numbers.";
            }
            return false;
        }

        if(!inputValidatorHelper.identicStrings(etPassword.getText().toString(), etPasswordConfirmed.getText().toString())) {
            errorMsg = "Your passwords do not match.";
            return false;
        }

        return true;
    }

}
