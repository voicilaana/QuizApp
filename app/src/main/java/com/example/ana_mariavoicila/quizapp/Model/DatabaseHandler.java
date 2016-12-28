package com.example.ana_mariavoicila.quizapp.Model;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ana_mariavoicila.quizapp.QuizQuestions;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Quiz_Database";

    private static final String TABLE_USERS = "Users";
    private static final String KEY_USER_ID = "U_id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PW = "userPw";
    private static final String KEY_USER_SCORE = "score";

    private static final String TABLE_QUESTIONS = "Questions";
    private static final String KEY_QUESTION_ID = "Q_id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_INDEX_CORRECT_ANSWER = "indexCorrectAnswer";

    private static final String TABLE_ANSWERS = "Answers";
    private static final String KEY_ANSWER_ID = "A_id";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_QUESTION_FK = "Q_id";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
            "(" +
            KEY_USER_ID + " int AUTO_INCREMENT, " +
            KEY_USER_NAME + " varchar, " +
            KEY_USER_PW + " varchar, " +
            KEY_USER_SCORE + " int, " +
            "PRIMARY KEY(" + KEY_USER_ID + ")" +
            " );";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS +
            "(" +
            KEY_QUESTION_ID + " int AUTO_INCREMENT, " +
            KEY_QUESTION + " TEXT, " +
            KEY_INDEX_CORRECT_ANSWER + " int NOT NULL, " +
            "PRIMARY KEY(" + KEY_QUESTION_ID + ")" +
            ");";

    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE " + TABLE_ANSWERS +
            "(" +
            KEY_ANSWER_ID + " int AUTO_INCREMENT, " +
            KEY_ANSWER + " TEXT, " +
            KEY_QUESTION_FK + " int NOT NULL, " +
            "FOREIGN KEY (" + KEY_QUESTION_FK + ") REFERENCES Questions(" + KEY_QUESTION_ID + ")" +
            ");";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_ANSWERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);

        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_USER_PW, user.getPassWord());
        values.put(KEY_USER_SCORE, user.getScore());

        db.insert(TABLE_USERS, null, values);
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues questionsValues = new ContentValues();
        ContentValues answersValues = new ContentValues();

        questionsValues.put(KEY_QUESTION, question.getQuestion());
        questionsValues.put(KEY_INDEX_CORRECT_ANSWER, question.getIndexCorrectAnswer());

        db.insert(TABLE_QUESTIONS, null, questionsValues);

        final int questionID = getLastQuestionId();

        for(String answer : question.getAnswers()) {
            answersValues.put(KEY_ANSWER, answer);
            answersValues.put(KEY_QUESTION_FK, questionID);
        }

        db.insert(TABLE_ANSWERS, null, answersValues);
    }

    private int getLastQuestionId() {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS +
                " WHERE " + KEY_QUESTION_ID + " = (SELECT MAX(" + KEY_QUESTION_ID + ") FROM " + TABLE_QUESTIONS + ") ";
        Log.e(DatabaseHandler.class.getName(), selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) do {
            id = (c.getInt(c.getColumnIndex(KEY_QUESTION_ID)));
        } while (c.moveToNext());

        c.close();

        return id;
    }

    public boolean isValidUsername(String username) {

        return false;
    }

    public boolean validCredentials(String userName, String password) {

        return false;
    }

    public void updateScore(String userName, int score) {

    }

    public User getUser() {

        return null;
    }

    public List<Question> getAllQuestions() {

        return null;
    }
}
