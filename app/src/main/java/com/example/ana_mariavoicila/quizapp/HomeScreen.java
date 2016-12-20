package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.app.Dialog;
import android.widget.Toast;


public class HomeScreen extends AppCompatActivity {

    Button buttonLogin, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //POINT 1: create the register SQL database & lister on click for login and register

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v) {
                Intent intentRegister = new Intent(getApplicationContext(),Register.class);
                startActivity(intentRegister);
            }
        });
    }

    public void login (View v)
    {
        final Dialog dialog = new Dialog(HomeScreen.this);

    }

}
