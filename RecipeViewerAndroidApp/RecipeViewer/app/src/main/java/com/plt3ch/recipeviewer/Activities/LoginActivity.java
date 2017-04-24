package com.plt3ch.recipeviewer.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.plt3ch.recipeviewer.Adapters.RecipesAdapter;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.Models.User;
import com.plt3ch.recipeviewer.R;
import com.plt3ch.recipeviewer.Validators;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        this.editTextPassword = (EditText) findViewById(R.id.editTextPassword);

//        Button buttonRegister = (Button)findViewById(R.id.registerButton);
//        buttonRegister.setOnClickListener(this);

        Button buttonLogin = (Button)findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        editTextUsername.setText("");
        editTextPassword.setText("");
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

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
//            case R.id.registerButton:
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
//                break;
            case R.id.loginButton:
                if (validateInputData()) {
                    new LoginUserFromService().execute(this.editTextUsername.getText().toString(),
                                                       this.editTextPassword.getText().toString());
                } else {
                    showWrongCredentialDialog();
                }
                break;
            default:
        }
    }

    private boolean validateInputData(){
        return Validators.isValidEmail(this.editTextUsername.getText().toString()) &&
                Validators.isPasswordValidate(this.editTextPassword.getText().toString());
    }

    private void showWrongCredentialDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle("Wrong credentials!");
        alertDialogBuilder.setMessage("Wrong username or password!");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class LoginUserFromService extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }
        }

        @Override
        protected void onPostExecute(String authToken) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if(!TextUtils.isEmpty(authToken)) {
                Intent intent = new Intent(LoginActivity.this, RecipesMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else{
                showWrongCredentialDialog();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            RecipeViewerController controller = RecipeViewerController.Instance();
            return controller.logUser(user);
        }
    }
}
