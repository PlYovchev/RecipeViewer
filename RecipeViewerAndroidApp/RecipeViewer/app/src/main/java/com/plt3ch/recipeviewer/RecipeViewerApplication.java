package com.plt3ch.recipeviewer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;

/**
 * Created by Plamen on 4/26/2017.
 */

public class RecipeViewerApplication extends Application {

    private static final String TAG = "RVApplication";

    public static final String SHARED_PREF_NAME = "recipeViewerSharedPrefs";

    private static Context context;

    private static Activity currentTopActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        RecipeViewerApplication.context = this.getApplicationContext();
        this.registerActivityLifecycleCallbacks(new RecipeViewerActivityLifecycleCallbacks());
    }

    public static Context getContext() {
        return context;
    }

    public static Activity getCurrentTopActivity() { return currentTopActivity; }

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

    private class RecipeViewerActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            RecipeViewerApplication.currentTopActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            clearTopActivityReference(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            clearTopActivityReference(activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            clearTopActivityReference(activity);
        }

        private void clearTopActivityReference(Activity activity) {
            if (activity != null && activity.equals(RecipeViewerApplication.currentTopActivity)) {
                RecipeViewerApplication.currentTopActivity = null;
            }
        }
    }

}
