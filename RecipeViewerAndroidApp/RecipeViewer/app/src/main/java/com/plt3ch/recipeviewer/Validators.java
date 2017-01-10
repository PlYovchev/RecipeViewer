package com.plt3ch.recipeviewer;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by plt3ch on 5/7/2015.
 */
public class Validators {

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,15}$";
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9.!]{6,15}$";

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isUsernameValidate(final String username){
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public final static boolean isPasswordValidate(final String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
