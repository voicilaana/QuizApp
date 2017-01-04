package com.example.ana_mariavoicila.quizapp;

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
        tvScore = (TextView) findViewById(R.id.tvScoreFinish);
        tvScore.setText(String.valueOf(DatabaseHandler.getInstance(getApplicationContext()).getUser().getScore()));

        buttonLeaderBoard = (Button) findViewById(R.id.buttonLeaderBoardFinish);
    }

    private void initListeners() {
        buttonLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Showing leaderboard..");
            }
        });
    }
}
