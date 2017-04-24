package com.plt3ch.recipeviewer.Authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;

/**
 * Created by Plamen on 4/25/2017.
 */

public class AuthenticationController {

    public static final String LAST_LOGGED_ACCOUNT_NAME_KEY = "lastLoggedAccountName";

    private static final String TAG = "AuthController";

    private static AuthenticationController authenticationController;

    private String lastLoggedUsername;

    private AuthenticationController() {}

    public static AuthenticationController getInstance() {
        if(authenticationController == null){
            authenticationController = new AuthenticationController();
        }

        return authenticationController;
    }

    public synchronized String getLastLoggedUsername(Context context) {
        if (TextUtils.isEmpty(lastLoggedUsername)) {
            SharedPreferences settings = context.getSharedPreferences(
                    RecipeViewerController.SHARED_PREF_NAME, 0);
            lastLoggedUsername = settings.getString(LAST_LOGGED_ACCOUNT_NAME_KEY, null);
        }
        return lastLoggedUsername;
    }

    public synchronized void saveLoggedUsername(Context context, String username) {
        if (TextUtils.isEmpty(username)) {
            Log.e(TAG, "Null or empty string is attempted to be saved for last logged username!");
            return;
        }

        // If the username is the same as the last logged, do nothing!
        if (username.equals(lastLoggedUsername)) {
            return;
        }

        SharedPreferences settings = context.getSharedPreferences(
                RecipeViewerController.SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAST_LOGGED_ACCOUNT_NAME_KEY, username);
        editor.commit();

        lastLoggedUsername = username;
    }

    public synchronized void clearLoggedUserInformation(Context context) {
        SharedPreferences settings = context.getSharedPreferences(
                RecipeViewerController.SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(LAST_LOGGED_ACCOUNT_NAME_KEY);
        editor.commit();

        lastLoggedUsername = null;
    }
}
