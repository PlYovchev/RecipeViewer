package com.plt3ch.recipeviewer.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Models.RegisterUser;
import com.plt3ch.recipeviewer.Models.User;
import com.plt3ch.recipeviewer.R;

public class RegisterActivity extends Activity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.emailEditText = (EditText) findViewById(R.id.etEmail);
        this.passwordEditText = (EditText) findViewById(R.id.etPassword);
        this.confirmPasswordEditText = (EditText) findViewById(R.id.etConfirmPassword);
        this.registerButton = (Button) findViewById(R.id.btnRegister);
        this.registerButton.setOnClickListener(new OnRegisterClickListener());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    private class OnRegisterClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            RegisterUser user = new RegisterUser();
            user.setEmail(emailEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.setConfirmPassword(confirmPasswordEditText.getText().toString());

            RecipeViewerController recipesController = RecipeViewerController.Instance();
            recipesController.registerUser(user);
        }
    }

}
