package com.example.ana_mariavoicila.quizapp.Model;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputValidator {

    public boolean isValidPassword(String password, boolean allowSpecialChars) {
        String PATTERN;

        if (allowSpecialChars) {
            PATTERN = "^[a-zA-Z@#$]\\w{5,19}$";
        } else {
            PATTERN = "^[a-zA-Z]\\w{5,19}$";
        }

        return password.matches(PATTERN);
    }

    public boolean isNullOrEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    public boolean identicStrings(String string1, String string2) {
        return string1.equals(string2);
    }

}

