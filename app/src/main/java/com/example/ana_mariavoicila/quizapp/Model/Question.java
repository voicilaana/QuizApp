package com.example.ana_mariavoicila.quizapp.Model;

import java.util.ArrayList;
import java.util.List;


public class Question {

    private String question;
    private int indexCorrectAnswer;
    private List<String> answers;
    private boolean answered;

    public Question() {
        question = "";
        indexCorrectAnswer = -1;
        answers = new ArrayList<>();
        answered = false;
    }

    public Question(String question, int indexCorrectAnswer, List<String> answers) {
        this.question = question;
        this.indexCorrectAnswer = indexCorrectAnswer;
        this.answers = answers;
        this.answered = false;
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

    public boolean isAnswered() { return answered; }

    public void setAnswered(boolean value) {
        answered = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        return question != null ? question.equals(question1.question) : question1.question == null;

    }
}
