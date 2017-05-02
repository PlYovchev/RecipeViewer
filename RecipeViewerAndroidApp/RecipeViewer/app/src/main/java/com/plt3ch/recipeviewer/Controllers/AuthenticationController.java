package com.plt3ch.recipeviewer.Controllers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Constants;
import com.plt3ch.recipeviewer.RecipeViewerApplication;

import java.io.IOException;

/**
 * Created by Plamen on 4/25/2017.
 */

public class AuthenticationController {

    public static final String LAST_LOGGED_ACCOUNT_NAME_KEY = "lastLoggedAccountName";

    private static final String TAG = "AuthController";

    private static AuthenticationController authenticationController;

    private String lastLoggedUsername;
    private Account mAccount;

    private AuthenticationController() {}

    public static AuthenticationController getInstance() {
        if(authenticationController == null){
            authenticationController = new AuthenticationController();
        }

        return authenticationController;
    }

    public synchronized String getLastLoggedUsername() {
        if (TextUtils.isEmpty(lastLoggedUsername)) {
            SharedPreferences settings = RecipeViewerApplication.getRecipeViewerPreferences();
            if (settings == null) {
                Log.d(TAG, "Didn't manage to retrieve the last logged username as " +
                        "didn't manage to retrieve the shared preferences!");
                return null;
            }
            lastLoggedUsername = settings.getString(LAST_LOGGED_ACCOUNT_NAME_KEY, null);
        }
        return lastLoggedUsername;
    }

    public synchronized void saveLoggedUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            Log.e(TAG, "Null or empty string is attempted to be saved for last logged username!");
            return;
        }

        // If the username is the same as the last logged, do nothing!
        if (username.equals(lastLoggedUsername)) {
            return;
        }

        SharedPreferences settings = RecipeViewerApplication.getRecipeViewerPreferences();
        if (settings == null) {
            Log.d(TAG, "Didn't manage to save the username as " +
                    "didn't manage to retrieve the shared preferences!");
            return;
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAST_LOGGED_ACCOUNT_NAME_KEY, username);
        editor.commit();

        lastLoggedUsername = username;
    }

    public synchronized void clearLoggedUserInformation() {
        lastLoggedUsername = null;
        mAccount = null;

        SharedPreferences settings = RecipeViewerApplication.getRecipeViewerPreferences();
        if (settings == null) {
            Log.d(TAG, "Didn't manage to clear the username as " +
                    "didn't manage to retrieve the shared preferences!");
            return;
        }

        SharedPreferences.Editor editor = settings.edit();
        editor.remove(LAST_LOGGED_ACCOUNT_NAME_KEY);
        editor.commit();
    }

    public synchronized Account getLoggedUserAccount() throws SecurityException {
        if (mAccount != null) {
            return mAccount;
        }

        String lastLoggedUsername = this.getLastLoggedUsername();
        if (lastLoggedUsername == null) {
            return null;
        }

        Account[] accounts = getAllRecipeViewerAccounts();

        // Check if there are any accounts created and if there are none,
        // hint the user to create a new one.
        if (accounts == null || accounts.length == 0) {
            return null;
        }

        for (Account account : accounts) {
            if (this.getLastLoggedUsername().equals(account.name)) {
                mAccount = account;
                return account;
            }
        }

        return null;
    }

    public Account[] getAllRecipeViewerAccounts() throws SecurityException {
        Account[] accounts = AccountManager.get(RecipeViewerApplication.getContext())
                .getAccountsByType(Constants.ACCOUNT_TYPE);
//        Log.e(TAG, "Something went wrong with retrieving the permissions " +
//                "for getting the accounts information!!!");

        return accounts;
    }

    public boolean verifyLoggedUserIsAuthenticated() {
        RecipesWebServiceController webContoller = new RecipesWebServiceController();
        return webContoller.checkIfLoggedUserTokenIsValid();
    }

    public String getLoggedUserAuthToken() {
        Account account = getLoggedUserAccount();
        if (account == null) {
            return null;
        }

        String authToken = null;
        try {
            authToken = AccountManager.get(RecipeViewerApplication.getContext())
                    .blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE, false);
        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
            e.printStackTrace();
        }

        return authToken;
    }

    public Account findExistingAccountToLogWith() {
        return null;
    }

    private void login() {

    }


}
