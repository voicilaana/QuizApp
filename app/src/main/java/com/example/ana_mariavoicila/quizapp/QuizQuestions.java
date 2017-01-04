package com.example.ana_mariavoicila.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
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
    private Button buttonSkip, buttonCheat, buttonNext;
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

    private void finishQuiz() {
        DatabaseHandler.getInstance(getApplicationContext()).updateScore(user);

        Intent intentFinish = new Intent(getApplicationContext(), Finish.class);
        intentFinish.putExtra("caller", "QuizQuestions");
        startActivity(intentFinish);
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
        buttonNext = (Button) findViewById(R.id.buttonNext);
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
                System.err.println("Caller undefined for QuizQuestions");
            }
        }

        listQuestions = DatabaseHandler.getInstance(getApplicationContext()).getAllQuestions();
        setSpinnerData();

        tvUsername.setText(user.getUserName());
        tvScore.setText(String.valueOf(user.getScore()));
        changeQuestion(currentQuestionIndex);
    }

    private void initListeners() {
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listQuestions.isEmpty()) {
                    if (currentQuestionIndex < listQuestions.size() && currentQuestionIndex >= 0 && questionsNotAnswered()) {
                            listQuestions.get(currentQuestionIndex).setAnswered(true);
                            currentQuestionIndex = getNextQuestionNotAnswered();
                            changeQuestion(currentQuestionIndex);
                    } else {
                        showMessage("No more questions to follow!");
                        finishQuiz();
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
                        }

                        listButtonAnswers.get(i).setEnabled(false);
                    }
                } else {
                    showMessage("No questions to be cheated!");
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestionIndex = getNextQuestionNotAnswered();
                if (currentQuestionIndex != -1) {
                    spinnerQuestions.setSelection(currentQuestionIndex);
                } else {
                    buttonNext.setText("Finish");
                    buttonNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishQuiz();
                        }
                    });
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

    private void generateRandomUser() {
        user = new User("anon-" + (int)(10000 * Math.random() + 100), "default-password");

        while (!DatabaseHandler.getInstance(getApplicationContext()).isValidUsername(user.getUserName())) {
            user.setUserName("anon-" + (int)(10000 * Math.random() + 100));
        }

        DatabaseHandler.getInstance(getApplicationContext()).addUser(user);
        DatabaseHandler.getInstance(getApplicationContext()).validCredentials(user.getUserName(), user.getPassWord());
    }

    protected void changeQuestion(int index) {
        if (!listQuestions.isEmpty()) {
            if (index < listQuestions.size() && index >= 0) {
                final Question question = listQuestions.get(index);
                tvQuestion.setText(question.getQuestion());
                List<String> answers = question.getAnswers();

                for (int i = 0; i < answers.size(); i++) {
                    listButtonAnswers.get(i).setText(answers.get(i));

                    if (i == question.getIndexCorrectAnswer()) {
                        listButtonAnswers.get(i).setOnClickListener(new AnswerButtonListener(true, question.isAnswered(), i, index));
                    } else {
                        listButtonAnswers.get(i).setOnClickListener(new AnswerButtonListener(false, question.isAnswered(), question.getIndexCorrectAnswer(), index));
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
        int i = currentQuestionIndex + 1;

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

    private class AnswerButtonListener implements View.OnClickListener {

        private boolean isCorrect;
        private boolean isAnswered;
        private int correctId;
        private int questionId;

        public AnswerButtonListener(boolean isCorrect, boolean isAnswered, int correctId, int questionId) {
            this.isCorrect = isCorrect;
            this.isAnswered = isAnswered;
            this.correctId = correctId;
            this.questionId = questionId;

            init();
        }

        private void init() {
            if (isAnswered && listButtonAnswers.get(0).isEnabled()) {
                disableButtons();
            } else if (!isAnswered && !listButtonAnswers.get(0).isEnabled()) {
                cleanButtons();
            }
        }

        private void cleanButtons() {
            for (Button button : listButtonAnswers) {
                button.setEnabled(true);
                button.setBackgroundResource(android.R.drawable.btn_default);
            }
        }

        private void disableButtons() {
            for (Button button : listButtonAnswers) {
                button.setEnabled(false);
            }
        }

        @Override
        public void onClick(View view) {
            if (view instanceof Button) {
                Button buttonAnswer = (Button) view;

                if (isCorrect) {
                    buttonAnswer.setBackgroundColor(Color.GREEN);
                    user.setScore(user.getScore() + 1);
                    tvScore.setText(String.valueOf(user.getScore()));

                    disableButtons();
                } else {
                    buttonAnswer.setBackgroundColor(Color.RED);
                    listButtonAnswers.get(correctId).setBackgroundColor(Color.GREEN);

                    disableButtons();
                }
            }

            listQuestions.get(questionId).setAnswered(true);
        }
    }
}
