package com.example.ana_mariavoicila.quizapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;
import com.example.ana_mariavoicila.quizapp.Model.User;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        init();
    }

    private void init() {
        // Construct the data source
        ArrayList<User> arrayOfUsers = DatabaseHandler.getInstance(getApplicationContext()).getAllUsers();
        UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.lvLeaderboard);
        listView.setAdapter(adapter);
    }

    private class UsersAdapter extends ArrayAdapter<User> {
        public UsersAdapter(Context context, ArrayList<User> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            }

            TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername_leaderboard);
            TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore_leaderboard);

            tvUsername.setText(user.getUserName());
            tvScore.setText(String.valueOf(user.getScore()));

            return convertView;
        }
    }
}
