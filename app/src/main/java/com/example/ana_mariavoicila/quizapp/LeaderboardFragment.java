package com.example.ana_mariavoicila.quizapp;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;
import com.example.ana_mariavoicila.quizapp.Model.User;

import java.util.List;

public class LeaderboardFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = new UsersAdapter(getActivity().getApplicationContext(), DatabaseHandler.getInstance(getActivity().getApplicationContext()).getLeaderboardData());
        setListAdapter(adapter);
    }

    private class UsersAdapter extends ArrayAdapter<User> {
        UsersAdapter(Context context, List<User> users) {
            super(context, 0, users);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            User user = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            }

            TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername_leaderboard);
            TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore_leaderboard);

            tvUsername.setText(user != null ? user.getUserName() : "undefined");
            tvScore.setText(String.valueOf(user != null ? user.getScore() : 0));

            return convertView;
        }
    }
}
