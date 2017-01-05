package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;


public class HomeScreen extends AppCompatActivity {

    private Button buttonLeaderboard, buttonRegister, buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initParams();
        initListeners();
    }

    private void initParams() {
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonLeaderboard = (Button) findViewById(R.id.buttonLeaderboard);
    }

    private void initListeners() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), Register.class);
                intentLogin.putExtra("caller", "HomeScreen");
                startActivity(intentLogin);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentModeS = new Intent(getApplicationContext(), ModeSelection.class);
                intentModeS.putExtra("caller", "HomeScreen");
                startActivity(intentModeS);
            }
        });

        buttonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLeaderboard = new Intent(getApplicationContext(), Leaderboard.class);
                intentLeaderboard.putExtra("caller", "HomeScreen");
                DatabaseHandler.getInstance(getApplicationContext()).setLeaderboardData(DatabaseHandler.getInstance(getApplicationContext()).getAllUsers());
                startActivity(intentLeaderboard);
            }
        });
    }
}
