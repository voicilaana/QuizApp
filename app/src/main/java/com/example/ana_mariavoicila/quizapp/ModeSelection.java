package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModeSelection extends AppCompatActivity {

    private Button buttonSinglePlayer, buttonMultiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);

        init();
    }

    private void init() {
        buttonSinglePlayer = (Button) findViewById(R.id.buttonSinglePlayer);
        buttonSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intentLogin = new Intent(getApplicationContext(), Login.class);
            intentLogin.putExtra("caller", "ModeSelection");
            intentLogin.putExtra("mode", "singleplayer");
            startActivity(intentLogin);
            }
        });

        buttonMultiPlayer = (Button) findViewById(R.id.buttonMultiPlayer);
        buttonMultiPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intentLogin = new Intent(getApplicationContext(), Login.class);
            intentLogin.putExtra("caller", "ModeSelection");
            intentLogin.putExtra("mode", "multiplayer");
            startActivity(intentLogin);
            }
        });
    }
}
