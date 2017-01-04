package com.example.ana_mariavoicila.quizapp.Model;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FileParser {

    private AssetManager assetManager;
    private InputStream inputStream;

    public FileParser(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public ArrayList<Question> getQuestions() {
        String data = getFileAsString("questions.txt");
        ArrayList<Question> questions = new ArrayList<Question>();

        String[] dataLines = data.split("\n");

        for (int i = 0; i < dataLines.length; i++) {
            if (i % 2 != 0) {
                String[] firstLineSplit = dataLines[i - 1].split("\\|");
                String[] secondLineSplit = dataLines[i].split("@n");

                Question q = new Question();
                q.setQuestion(firstLineSplit[0]);
                q.setIndexCorrectAnswer(Integer.parseInt(firstLineSplit[1]) - 1);

                for (int j = 0; j < secondLineSplit.length; j++) {
                    q.getAnswers().add(secondLineSplit[j].trim());
                }

                questions.add(q);
            }
        }

        return questions;
    }

    private String getFileAsString(String fileName) {
        String fileString = "";

        try {
            inputStream = assetManager.open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            fileString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileString;
    }
}
