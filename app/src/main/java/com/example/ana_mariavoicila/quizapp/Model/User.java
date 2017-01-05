package com.example.ana_mariavoicila.quizapp.Model;

public class User {

    private int id;
    private String userName;
    private String passWord;
    private int score;

    public User() {
        this.userName = "";
        this.passWord = "";
        this.score = 0;
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.score = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null)
            return false;

        return true;
    }
}
