package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final Button buttonStart = (Button) findViewById(R.id.buttonStart);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        final Button buttonLeaderboard = (Button) findViewById(R.id.buttonLeaderboard);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(HomeScreen.this,Register.class);
                HomeScreen.this.startActivity(registerIntent);
            }
        });
    }

    private void playGame() {
        System.out.println("Works");
    }
}
