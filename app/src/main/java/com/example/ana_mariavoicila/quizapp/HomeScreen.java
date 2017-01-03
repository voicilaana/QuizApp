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

    private Button buttonLeaderboard, buttonLoginRegister, buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initParams();
        initListeners();
    }

    private void initParams() {
        // TODO: Should add help button with instructions and about
        // TODO: Should add logo "The Quiz App" - maybe a colorful Q
        buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonLeaderboard = (Button) findViewById(R.id.buttonLeaderboard);
    }

    private void initListeners() {
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), Login.class);
                intentLogin.putExtra("caller", "HomeScreen");
                startActivity(intentLogin);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentStart = new Intent(getApplicationContext(), QuizQuestions.class);
                intentStart.putExtra("caller", "HomeScreen");
                startActivity(intentStart);
                // TODO: Should ask if wants to login or play as anon
                // Start the appropriate activity
            }
        });

        buttonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Showing leaderboard...");
            }
        });
    }
}
