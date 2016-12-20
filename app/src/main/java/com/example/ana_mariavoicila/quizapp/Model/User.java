package com.example.ana_mariavoicila.quizapp.Model;

/**
 * Created by ana-mariavoicila on 20/12/2016.
 */

public class User {

    int id;
    String name;
    String userName;
    String passWord;
    double score;

    public User(int id, String name, String userName, String passWord, double score) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.passWord = passWord;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
