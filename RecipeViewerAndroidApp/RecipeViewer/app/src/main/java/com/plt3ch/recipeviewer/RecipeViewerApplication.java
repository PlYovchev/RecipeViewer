package com.plt3ch.recipeviewer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;

/**
 * Created by Plamen on 4/26/2017.
 */

public class RecipeViewerApplication extends Application {

    private static final String TAG = "RVApplication";

    public static final String SHARED_PREF_NAME = "recipeViewerSharedPrefs";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        RecipeViewerApplication.context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static SharedPreferences getRecipeViewerPreferences() {
        if (context == null) {
            Log.d(TAG, "The shared preferences can't be retrieved because " +
                    "the app context is not created yet!");
            return null;
        }

        SharedPreferences settings = context.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        return settings;
    }
}
