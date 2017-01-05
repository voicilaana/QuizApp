package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
        buttonLeaderBoard = (Button) findViewById(R.id.buttonLeaderBoardFinish);
    }

    private void initListeners() {
        buttonLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLeaderboard = new Intent(getApplicationContext(), Leaderboard.class);
                intentLeaderboard.putExtra("caller", "Finish");
                DatabaseHandler.getInstance(getApplicationContext()).setLeaderboardData(DatabaseHandler.getInstance(getApplicationContext()).getAllUsers());
                startActivity(intentLeaderboard);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseHandler.getInstance(getApplicationContext()).logoutUsers();
    }
}
