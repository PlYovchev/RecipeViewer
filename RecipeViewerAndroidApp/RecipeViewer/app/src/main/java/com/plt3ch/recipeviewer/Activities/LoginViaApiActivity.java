package com.plt3ch.recipeviewer.Activities;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;

import com.plt3ch.recipeviewer.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginViaApiActivity extends AccountAuthenticatorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_via_api);

    }
}

