package com.example.ana_mariavoicila.quizapp.Model;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ana-mariavoicila on 26/12/2016.
 */

public class InputValidator {

    public boolean isValidPassword(String string, boolean allowSpecialChars) {
        String PATTERN;
        if (allowSpecialChars) {
            PATTERN = "^[a-zA-Z@#$]\\w{5,19}$";
        } else {
            PATTERN = "^[a-zA-Z]\\w{5,19}$";
        }
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public boolean isNullOrEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    public boolean identicStrings(String string1, String string2) {
        return string1.equals(string2);
    }

}

