package com.example.ana_mariavoicila.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;
import com.example.ana_mariavoicila.quizapp.Model.User;

public class Login extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button buttonLogin;
    private Button buttonAnonymousStart;
    private Toast toast;
    private TextView tvPlayerNumber;

    private int numberOfUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initParams();
        checkMode();
    }

    private void checkMode() {
        String caller = getIntent().getStringExtra("caller");
        if (caller.equals("ModeSelection")) {
            String mode = getIntent().getStringExtra("mode");
            if (mode.equals("singleplayer")) {
                initSinglePlayerListeners();
            } else if (mode.equals("multiplayer")) {
                initMultiPlayerListeners();
            }
        }
    }

    @SuppressLint({"ShowToast"})
    private void initParams() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonAnonymousStart = (Button) findViewById(R.id.buttonAnonymousStart);
        tvPlayerNumber = (TextView) findViewById(R.id.tvPlayerNumber);
        tvPlayerNumber.setText(R.string.tvDefaultPlayerNumber);

        toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);

        // Number of players allowed to play the game in multiplayer mode.
        numberOfUsers = 2;
    }

    private void initSinglePlayerListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(DatabaseHandler.getInstance(getApplicationContext()).validCredentials(etUsername.getText().toString(), etPassword.getText().toString())) {
                    Intent quizQuestionsIntent = new Intent(getApplicationContext(), QuizQuestions.class);
                    quizQuestionsIntent.putExtra("caller", "Login");
                    startActivityForResult(quizQuestionsIntent, 1);
                    finish();
                } else {
                    invalidMessage("Username and password do not match.");
                }
            }
        });

        buttonAnonymousStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomUser();

                Intent quizQuestionsIntent = new Intent(getApplicationContext(), QuizQuestions.class);
                quizQuestionsIntent.putExtra("caller", "Anonymous");
                startActivityForResult(quizQuestionsIntent, 1);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void resetViewForNewUser() {
        etUsername.setText("");
        etPassword.setText("");
        int playerNumber = DatabaseHandler.getInstance(getApplicationContext()).getLoggedInUsers().size() + 1;
        tvPlayerNumber.setText("Player " + playerNumber);
    }

    private void initMultiPlayerListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(DatabaseHandler.getInstance(getApplicationContext()).validCredentials(etUsername.getText().toString(), etPassword.getText().toString())) {
                    resetViewForNewUser();

                    if (numberOfUsers == DatabaseHandler.getInstance(getApplicationContext()).getLoggedInUsers().size()) {
                        Intent quizQuestionsIntent = new Intent(getApplicationContext(), QuizQuestions.class);
                        quizQuestionsIntent.putExtra("caller", "Login");
                        quizQuestionsIntent.putExtra("number_of_users", numberOfUsers);
                        startActivityForResult(quizQuestionsIntent, 1);
                        finish();
                    }
                } else {
                    invalidMessage("Username and password do not match.");
                }
            }
        });

        buttonAnonymousStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomUser();
                resetViewForNewUser();

                if (numberOfUsers == DatabaseHandler.getInstance(getApplicationContext()).getLoggedInUsers().size()) {
                    Intent quizQuestionsIntent = new Intent(getApplicationContext(), QuizQuestions.class);
                    quizQuestionsIntent.putExtra("caller", "Login");
                    quizQuestionsIntent.putExtra("number_of_users", numberOfUsers);
                    startActivityForResult(quizQuestionsIntent, 1);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            DatabaseHandler.getInstance(getApplicationContext()).logoutUsers();
            etUsername.setText("");
            etPassword.setText("");
            finish();
        }
    }

    private User generateRandomUser() {
        User user = new User("anon-" + (int)(10000 * Math.random() + 100), "default-password");

        while (!DatabaseHandler.getInstance(getApplicationContext()).isValidUsername(user.getUserName())) {
            user.setUserName("anon-" + (int)(10000 * Math.random() + 100));
        }

        DatabaseHandler.getInstance(getApplicationContext()).addUser(user);
        DatabaseHandler.getInstance(getApplicationContext()).validCredentials(user.getUserName(), user.getPassWord());
        user = DatabaseHandler.getInstance(getApplicationContext()).getUser(user.getUserName());

        return user;
    }

    private void invalidMessage(String errorMsg) {
        toast.setText(errorMsg);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseHandler.getInstance(getApplicationContext()).logoutUsers();
    }
}
