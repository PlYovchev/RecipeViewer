package com.plt3ch.recipeviewer.Activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Controllers.AuthenticationController;
import com.plt3ch.recipeviewer.Constants;
import com.plt3ch.recipeviewer.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    /** The tag used to log to adb console. **/
    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 123;

    private AccountManager mAccountManager;
    private AuthenticationController mAuthenticationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountManager = AccountManager.get(this);
        mAuthenticationController = AuthenticationController.getInstance();

        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    tryLoginWithExistingAccount();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.GET_ACCOUNTS},
                            MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
                }
//                AccountManager.get(MainActivity.this)
//                        .addAccount(Constants.ACCOUNT_TYPE, Constants.AUTHTOKEN_TYPE, null, null,
//                                MainActivity.this, null, null);
//                Intent intentToLoginActivity = new Intent(getBaseContext(), LoginActivity.class);
//                startActivity(intentToLoginActivity);
            }
        });
    }

    private void tryLoginWithExistingAccount() {
        Account account = mAuthenticationController.getLoggedUserAccount();
        if (account == null) {
            Account[] accounts = mAuthenticationController.getAllRecipeViewerAccounts();
            // Check if there are any accounts created and if there are none,
            // hint the user to create a new one.
            if (accounts == null || accounts.length == 0) {
                // TODO: prompt to create account
                return;
            }

            if (accounts.length == 1) {
                account = accounts[0];
                mAuthenticationController.saveLoggedUsername(account.name);
            }
        }

        if (account != null) {
            if (mAuthenticationController.verifyLoggedUserIsAuthenticated()) {
                Intent intentToLoginActivity = new Intent(this, LoginActivity.class);
                startActivity(intentToLoginActivity);
            }
        }
    }


    private void promtUserToSelectAccount() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tryLoginWithExistingAccount();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
