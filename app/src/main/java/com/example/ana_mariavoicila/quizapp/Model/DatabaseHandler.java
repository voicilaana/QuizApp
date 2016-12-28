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

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Quiz_Database";

    private static final String TABLE_USERS = "Users";
    private static final String KEY_USER_ID = "U_id";
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
            KEY_USER_ID + " int NOT NULL AUTO_INCREMENT, " +
            KEY_USER_NAME + " varchar(100) NOT NULL, " +
            KEY_USER_PW + " varchar(64) NOT NULL, " +
            "PRIMARY KEY(" + KEY_USER_ID + ")" +
            " );";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS +
            "(" +
            KEY_QUESTION_ID + " int NOT NULL AUTO_INCREMENT, " +
            KEY_QUESTION + " TEXT, " +
            KEY_INDEX_CORRECT_ANSWER + " int NOT NULL, " +
            "PRIMARY KEY(" + KEY_QUESTION_ID + ")" +
            ");";

    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE " + TABLE_ANSWERS +
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

        db.insert(TABLE_USERS, null, values);
    }

//    public void addQuestion(Question question) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues questionsValues = new ContentValues();
//        ContentValues answersValues = new ContentValues();
//
//        questionsValues.put(KEY_QUESTION, question.getQuestion());
//        questionsValues.put(KEY_INDEX_CORRECT_ANSWER, question.getIndexCorrectAnswer());
//
//        db.insert(TABLE_QUESTIONS, null, questionsValues);
//
//        for(String answer : question.getAnswers()) {
//            questionsValues.put(KEY_ANSWER, answer);
//            questionsValues.put(KEY_QUESTION_FK, questionID); // TODO: get the question id from db
//        }
//    }

    public void getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        Log.e(DatabaseHandler.class.getName(), selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) do {
            System.out.println(c.getString(c.getColumnIndex(KEY_USER_NAME)));
        } while (c.moveToNext());

        c.close();
    }

    public boolean isValidUsername(String username) {
        return false;
    }

    public boolean isLoggedIn(String userName, String password) {
        return false;
    }

    // TODO: add user with dynamic id (auto increase)

    // TODO: methods: addQuestion, getAllQuestions as an array of Question
}
