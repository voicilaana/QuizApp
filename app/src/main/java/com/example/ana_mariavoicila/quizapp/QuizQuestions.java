package com.example.ana_mariavoicila.quizapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ana_mariavoicila.quizapp.Model.DatabaseHandler;
import com.example.ana_mariavoicila.quizapp.Model.Question;
import com.example.ana_mariavoicila.quizapp.Model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvUsername;
    private Button buttonSkip, buttonCheat;
    private List<Button> listButtonAnswers;
    private Spinner spinnerQuestions;

    private List<Question> listQuestions;
    private User user;
    private int currentQuestionIndex;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        initParams();
        initListeners();
    }

    private void initParams() {
        toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
        currentQuestionIndex = 0;
        listButtonAnswers = new ArrayList<Button>();

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        buttonSkip = (Button) findViewById(R.id.buttonSkip);
        buttonCheat = (Button) findViewById(R.id.buttonCheat);
        spinnerQuestions = (Spinner) findViewById(R.id.spinnerQuestions);

        listButtonAnswers.add((Button) findViewById(R.id.buttonAnswer1));
        listButtonAnswers.add((Button) findViewById(R.id.buttonAnswer2));
        listButtonAnswers.add((Button) findViewById(R.id.buttonAnswer3));
        listButtonAnswers.add((Button) findViewById(R.id.buttonAnswer4));

        user = DatabaseHandler.getInstance(getApplicationContext()).getUser();

        String caller = "";
        if (user == null) {
            caller = getIntent().getStringExtra("caller");

            if (caller.equals("HomeScreen")) {
                generateRandomUser();
            } else {
                showMessage("Caller undefined.");
            }
        }

        listQuestions = DatabaseHandler.getInstance(getApplicationContext()).getAllQuestions();
        setSpinnerData();

        tvUsername.setText(user.getUserName());
        tvScore.setText(String.valueOf(user.getScore()));
        changeQuestion(currentQuestionIndex);
    }

    private void generateRandomUser() {
        user = new User("anon-" + (int)(10000 * Math.random() + 100), "default-password");

        while (!DatabaseHandler.getInstance(getApplicationContext()).isValidUsername(user.getUserName())) {
            user.setUserName("anon-" + (int)(10000 * Math.random() + 100));
        }

        DatabaseHandler.getInstance(getApplicationContext()).addUser(user);
        DatabaseHandler.getInstance(getApplicationContext()).validCredentials(user.getUserName(), user.getPassWord());
    }

    private void initListeners() {
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listQuestions.isEmpty()) {
                    listQuestions.get(currentQuestionIndex).setAnswered(true);

                    currentQuestionIndex += 1;
                    if (currentQuestionIndex < listQuestions.size()) {
                        changeQuestion(currentQuestionIndex);
                    } else {
                        if (questionsNotAnswered()) {
                            currentQuestionIndex = getNextQuestionNotAnswered();
                            changeQuestion(currentQuestionIndex);
                        } else {
                            showMessage("No more questions to follow!");
                            showMessage("Should finish the game and show score..");
                            // TODO: Should display the score in a new activity along with a button to leaderboard.
                        }
                    }
                } else {
                    showMessage("No questions to be skipped!");
                }
            }
        });

        buttonCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listQuestions.isEmpty()) {
                    listQuestions.get(currentQuestionIndex).setAnswered(true);

                    for (int i = 0; i < listButtonAnswers.size(); i++) {
                        if (i == listQuestions.get(currentQuestionIndex).getIndexCorrectAnswer()) {
                            listButtonAnswers.get(i).setBackgroundColor(Color.GREEN);
                        } else {
                            listButtonAnswers.get(i).setBackgroundColor(Color.RED);
                            listButtonAnswers.get(i).setEnabled(false);
                        }
                    }

                    showMessage("Press the correct answer to go to the next question.");
                } else {
                    showMessage("No questions to be cheated!");
                }
            }
        });

        spinnerQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentQuestionIndex = i;
                changeQuestion(currentQuestionIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    protected void changeQuestion(int index) {
        if (!listQuestions.isEmpty()) {
            final Question question = listQuestions.get(index);
            tvQuestion.setText(question.getQuestion());
            List<String> answers = question.getAnswers();

            for (int i = 0; i < answers.size(); i++) {
                listButtonAnswers.get(i).setText(answers.get(i));

                if (!question.isAnswered()) {
                    if (i == question.getIndexCorrectAnswer()) {
                        listButtonAnswers.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                question.setAnswered(true);
                                user.setScore(user.getScore() + 1);
                                listButtonAnswers.get(currentQuestionIndex).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currentQuestionIndex += 1;
                                        changeQuestion(currentQuestionIndex);
                                    }
                                });
                            }
                        });
                    }
                } else {
                    listButtonAnswers.get(i).setEnabled(false);
                    listButtonAnswers.get(i).setBackgroundColor(Color.RED);

                    if (i == question.getIndexCorrectAnswer()) {
                        listButtonAnswers.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                question.setAnswered(true);
                            }
                        });
                        listButtonAnswers.get(i).setBackgroundColor(Color.GREEN);
                    }
                }
            }
        } else {
            showMessage("The app does not have questions loaded.");
        }
    }

    private void setSpinnerData() {
        ArrayList<String> questionsString = new ArrayList<String>();

        for (Question q : listQuestions) {
            questionsString.add(q.getQuestion());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, questionsString);
        spinnerQuestions.setAdapter(adapter);
        spinnerQuestions.setSelection(currentQuestionIndex);
    }

    private boolean questionsNotAnswered() {
        for (Question question : listQuestions) {
            if (!question.isAnswered()) {
                return true;
            }
        }

        return false;
    }

    private int getNextQuestionNotAnswered() {
        int i = currentQuestionIndex;

        if (i >= listQuestions.size()) {
            i = 0;
        }

        for (; i < listQuestions.size(); i++) {
            if (!listQuestions.get(i).isAnswered()) {
                return i;
            }
        }

        return -1;
    }

    private void showMessage(String message) {
        toast.setText(message);
        toast.show();
    }
}
