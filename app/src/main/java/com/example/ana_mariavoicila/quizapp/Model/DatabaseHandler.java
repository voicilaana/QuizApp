package com.example.ana_mariavoicila.quizapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PrivateKey;

/**
 * Created by ana-mariavoicila on 20/12/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Quiz_Database";

    private static final String TABLE_USERS = "Users";
    private static final String KEY_USER_ID = "U_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PW = "userPw";
    private static final String KEY_SCORE = "score";

    private static final String TABLE_QUESTIONS = "Questions";
    private static final String KEY_QUESTION_ID = "Q_id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_INDEX_CORRECT_ANSWER = "indexCorrectAnswer";

    private static final String TABLE_ANSWERS = "Answers";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_QUESTION_FK = "Q_id";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
            "(" +
            KEY_USER_ID + " int NOT NULL," +
            KEY_NAME + " varchar(100)," +
            KEY_USER_NAME + " varchar(100) NOT NULL," +
            KEY_USER_PW + " varchar(64) NOT NULL," +
            KEY_SCORE + " double" +
            ");";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS +
            "(" +
            KEY_QUESTION_ID + " int NOT NULL," +
            KEY_QUESTION + " TEXT," +
            KEY_INDEX_CORRECT_ANSWER + " int NOT NULL" +
            ");";

    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE" + TABLE_ANSWERS +
            "(" +
            KEY_ANSWER + " TEXT," +
            KEY_QUESTION_FK + " int NOT NULL," +
            "FOREIGN KEY (" + KEY_QUESTION_FK + ") REFERENCES Questions(" + KEY_QUESTION_ID + ")" +
            ");";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS);
        db.execSQL(TABLE_QUESTIONS);
        db.execSQL(TABLE_ANSWERS);
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

        values.put(KEY_USER_ID, user.getId());
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_USER_PW, user.getPassWord());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_SCORE, user.getScore());

        db.insert(TABLE_USERS, null, values);
    }

    public boolean isValidUsername(String username) {
        return false;
    }

    public boolean isLoggedIn(String userName, String password) {
        return false;
    }

    // creem user (verificam daca username-ul exista) -> daca exista sugeram sa login sau sa aleaga altul
    // SAU
    // ne logam -> verificam username & parola

    // getName for a userName
    // getQuestions && getAnswers -> concurently create new question objects
}
