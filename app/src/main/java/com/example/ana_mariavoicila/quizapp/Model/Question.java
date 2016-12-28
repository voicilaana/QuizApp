package com.example.ana_mariavoicila.quizapp.Model;

import java.util.ArrayList;
import java.util.List;


public class Question {

    private String question;
    private int indexCorrectAnswer;
    private List<String> answers;

    public Question(String question, int indexCorrectAnswer, List<String> answers) {
        this.question = question;
        this.indexCorrectAnswer = indexCorrectAnswer;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getIndexCorrectAnswer() {
        return indexCorrectAnswer;
    }

    public void setIndexCorrectAnswer(int indexCorrectAnswer) {
        this.indexCorrectAnswer = indexCorrectAnswer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
