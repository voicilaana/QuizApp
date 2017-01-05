package com.example.ana_mariavoicila.quizapp.Model;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;

import com.example.ana_mariavoicila.quizapp.QuizQuestions;

import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
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


    private static DatabaseHandler sInstance;
    private List<User> loggedInUsers;
    private Context context;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        loggedInUsers = new ArrayList<User>();
    }

    public static synchronized DatabaseHandler getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
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
        String dbUsername = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + "='" + username + "'";
        Log.e(DatabaseHandler.class.getName(), selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) do {
            dbUsername = (c.getString(c.getColumnIndex(KEY_USER_NAME)));
        } while (c.moveToNext());

        c.close();

        if (dbUsername != null && dbUsername.equals(username)) {
            return false;
        }

        return true;
    }

    public boolean validCredentials(String userName, String password) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + "='" + userName + "' AND " + KEY_USER_PW + "='" + password + "'";
        Log.e(DatabaseHandler.class.getName(), selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) do {
            user = new User();
            user.setUserName(c.getString(c.getColumnIndex(KEY_USER_NAME)));
            user.setPassWord(c.getString(c.getColumnIndex(KEY_USER_PW)));
            user.setId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
            user.setScore(c.getInt(c.getColumnIndex(KEY_USER_SCORE)));
        } while (c.moveToNext());

        c.close();

        if (user != null) {
            loggedInUsers.add(user);
            return true;
        }

        return false;
    }

    public User getUser() {
        if (!loggedInUsers.isEmpty()) {
            return loggedInUsers.get(loggedInUsers.size() - 1);
        }

        return null;
    }

    public int updateScore(User user) {
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_USER_PW, user.getPassWord());
        values.put(KEY_USER_SCORE, user.getScore());

        return this.getWritableDatabase().update(TABLE_USERS, values, KEY_USER_NAME + "='" + user.getUserName() + "'", null);
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<Question>();
        String selectQuestionsQuery = "SELECT * FROM " + TABLE_QUESTIONS;
        String selectAnswersQuery = "SELECT * FROM " + TABLE_ANSWERS;
        String whereStatement;

        Log.e(DatabaseHandler.class.getName(), selectQuestionsQuery);
        Cursor qstC = this.getReadableDatabase().rawQuery(selectQuestionsQuery, null);
        Cursor ansC = null;

        if (qstC.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(qstC.getString((qstC.getColumnIndex(KEY_QUESTION))));
                question.setIndexCorrectAnswer(qstC.getInt(qstC.getColumnIndex(KEY_INDEX_CORRECT_ANSWER)));

                Log.e(DatabaseHandler.class.getName(), selectQuestionsQuery);
                whereStatement = " WHERE " + KEY_QUESTION_FK + "='" + qstC.getInt(qstC.getColumnIndex(KEY_QUESTION_ID)) + "'";
                ansC = this.getReadableDatabase().rawQuery(selectAnswersQuery + whereStatement, null);
                List<String> answers = new ArrayList<String>();

                if (ansC.moveToFirst()) {
                    do {
                        answers.add(ansC.getString(ansC.getColumnIndex(KEY_ANSWER)));
                    } while (ansC.moveToNext());
                }

                question.setAnswers(answers);
                questions.add(question);
            } while (qstC.moveToNext());
        }

        if (questions.isEmpty()) {
            questions = getQuestionsFromFile();
        }

        return questions;
    }

    private ArrayList<Question> getQuestionsFromFile() {
        ArrayList<Question> questions = new ArrayList<Question>();
        FileParser parser = new FileParser(context.getAssets());

        questions = parser.getQuestions();

        return questions;
    }

    public void logoutUser(String username) {
        for (User user : loggedInUsers) {
            if (user.getUserName().equals(username)) {
                loggedInUsers.remove(user);
                break;
            }
        }
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();
        String selectUsersQuery = "SELECT * FROM " + TABLE_USERS;
        String whereStatement;

        Log.e(DatabaseHandler.class.getName(), selectUsersQuery);
        Cursor usersCursor = this.getReadableDatabase().rawQuery(selectUsersQuery, null);

        if (usersCursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserName(usersCursor.getString(usersCursor.getColumnIndex(KEY_USER_NAME)));
                user.setPassWord(usersCursor.getString(usersCursor.getColumnIndex(KEY_USER_PW)));
                user.setId(usersCursor.getInt(usersCursor.getColumnIndex(KEY_USER_ID)));
                user.setScore(usersCursor.getInt(usersCursor.getColumnIndex(KEY_USER_SCORE)));
                users.add(user);
            } while (usersCursor.moveToNext());
        }

        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User a, User b) {
                return a.getScore() < b.getScore() ? 1 : a.getScore() == b.getScore() ? 0 : -1;
            }
        });

        return users;
    }
}
