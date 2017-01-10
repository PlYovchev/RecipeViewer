package com.plt3ch.recipeviewer.ViewConfigureStrategies;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Activities.RecipeDetailsActivity;
import com.plt3ch.recipeviewer.Adapters.RecipiesAdapter;
import com.plt3ch.recipeviewer.Models.Recipe;

import java.util.List;

/**
 * Created by Plamen on 1/9/2017.
 */

public class RecipiesViewStrategy extends ViewStrategy {

    public static final String INTENT_RECIPE_KEY = "recipeKey";

    private List<Recipe> recipies;
    private ListView recipiesView;

    public RecipiesViewStrategy(Context context, ListView recipiesView) {
        super(context);
        this.recipiesView = recipiesView;
    }

    public void configureRecipiesListView() {
        loadRecipiesDataSet();
        if (this.recipiesView == null) {
            Toast.makeText(mContext, "Something went wrong with the view!", Toast.LENGTH_LONG).show();
            return;
        } else if (this.recipies == null) {
            Toast.makeText(mContext, "Failed to retrieve the recipes!", Toast.LENGTH_LONG).show();
            return;
        } else if (this.recipies.size() == 0) {
            Toast.makeText(mContext, "There aren't any recipes to show!", Toast.LENGTH_LONG).show();
            return;
        }

        populateRecipiesListView();
    }

    private void loadRecipiesDataSet() {
//        TestData testData = new TestData();
//        this.recipies = testData.getRecipies();
    }

    private void populateRecipiesListView() {
        RecipiesAdapter adapter = new RecipiesAdapter(recipies, mContext);
        this.recipiesView.setAdapter(adapter);

        this.recipiesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipies.get(position);
                navigateToRecipeDetailsView(recipe);
            }
        });
    }

    private void navigateToRecipeDetailsView(Recipe recipe) {
        Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
        intent.putExtra(INTENT_RECIPE_KEY, recipe);
        mContext.startActivity(intent);
    }
}
