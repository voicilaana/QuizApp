package com.example.ana_mariavoicila.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvUsername;
    private Button buttonSkip, buttonCheat, buttonNext;
    private List<Button> listButtonAnswers;
    private Spinner spinnerQuestions;

    private List<Question> listQuestions;
    private List<Question> allQuestions;
    private List<User> users;
    private User currentUser;
    private int currentUserIndex;
    private int currentQuestionIndex;
    private int numberOfQuestionsPerRound;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        initParams();
        initListeners();
    }

    @SuppressLint("ShowToast")
    private void initParams() {
        numberOfQuestionsPerRound = 7;
        currentQuestionIndex = 0;
        currentUserIndex = 0;
        users = DatabaseHandler.getInstance(getApplicationContext()).getLoggedInUsers();
        currentUser = users.get(currentUserIndex);

        toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
        listButtonAnswers = new ArrayList<>();

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

        allQuestions = DatabaseHandler.getInstance(getApplicationContext()).getAllQuestions();
        listQuestions = new ArrayList<>();
        listQuestions = getRandomQuestions();
        setSpinnerData();

        tvUsername.setText(currentUser.getUserName());
        tvScore.setText(String.valueOf(currentUser.getScore()));
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
                        finishRound();
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
                    if (currentQuestionIndex < listQuestions.size() && currentQuestionIndex >= 0 && questionsNotAnswered()) {
                        listQuestions.get(currentQuestionIndex).setAnswered(true);

                        for (int i = 0; i < listButtonAnswers.size(); i++) {
                            if (i == listQuestions.get(currentQuestionIndex).getIndexCorrectAnswer()) {
                                listButtonAnswers.get(i).setBackgroundColor(Color.GREEN);
                            }

                            listButtonAnswers.get(i).setEnabled(false);
                        }
                    } else {
                        showMessage("No more questions to cheat!");
                        finishRound();
                    }
                } else {
                    showMessage("No questions to be cheated!");
                }
            }
        });

        initButtonNext();

        spinnerQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentQuestionIndex = i;
                changeQuestion(currentQuestionIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // ...
            }
        });
    }

    private void initButtonNext() {
        buttonNext.setText(R.string.nextButton);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestionIndex = getNextQuestionNotAnswered();
                if (currentQuestionIndex != -1) {
                    spinnerQuestions.setSelection(currentQuestionIndex);
                } else {
                    buttonNext.setText(R.string.finishButton);
                    buttonNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishRound();
                        }
                    });
                }
            }
        });
    }

    private void finishRound() {
        DatabaseHandler.getInstance(getApplicationContext()).updateScore(currentUser);

        if (currentUser.equals(users.get(users.size() - 1))) {
            finishQuiz();
        } else {
            resetForNextPlayer();
        }
    }

    private void resetForNextPlayer() {
        currentUser = users.get(++currentUserIndex);
        listQuestions = getRandomQuestions();
        setSpinnerData();

        tvUsername.setText(currentUser.getUserName());
        tvScore.setText(String.valueOf(currentUser.getScore()));
        currentQuestionIndex = 0;
        changeQuestion(currentQuestionIndex);

        initButtonNext();
    }

    private void finishQuiz() {
        Intent intentFinish = new Intent(getApplicationContext(), Finish.class);
        intentFinish.putExtra("caller", "QuizQuestions");
        DatabaseHandler.getInstance(getApplicationContext()).setLeaderboardData(users);
        startActivity(intentFinish);
        finish();
    }

    private List<Question> getRandomQuestions() {
        List<Question> questions = new ArrayList<>();
        boolean notEnough = true;
        int randomIndex = 0;

        while (notEnough) {
            if (!listQuestions.contains(allQuestions.get(randomIndex))) {
                questions.add(allQuestions.get(randomIndex));
            }
            randomIndex = (int) ((allQuestions.size() - 1) * Math.random());

            if (questions.size() == numberOfQuestionsPerRound) {
                notEnough = false;
            }
        }

        return questions;
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
        ArrayList<String> questionsString = new ArrayList<>();

        for (Question q : listQuestions) {
            questionsString.add(q.getQuestion());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, questionsString);
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

        AnswerButtonListener(boolean isCorrect, boolean isAnswered, int correctId, int questionId) {
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
                    currentUser.setScore(currentUser.getScore() + 1);
                    tvScore.setText(String.valueOf(currentUser.getScore()));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseHandler.getInstance(getApplicationContext()).logoutUsers();
    }
}
