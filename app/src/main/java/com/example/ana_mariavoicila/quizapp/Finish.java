package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;

public class Finish extends AppCompatActivity {

    private TextView tvScore;
    private Button buttonLeaderBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        initParams();
        initListeners();
    }

    private void initParams() {

        // TODO: add fragment with leaderboard for the users who played.
        buttonLeaderBoard = (Button) findViewById(R.id.buttonLeaderBoardFinish);
    }

    private void initListeners() {
        buttonLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLeaderboard = new Intent(getApplicationContext(), Leaderboard.class);
                intentLeaderboard.putExtra("caller", "Finish");
                startActivity(intentLeaderboard);
            }
        });
    }
}
