package com.plt3ch.recipeviewer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Adapters.IngredientsAdapter;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerDatabase;
import com.plt3ch.recipeviewer.Fragments.RecipeDetailsFragment;
import com.plt3ch.recipeviewer.Fragments.RecipesListFragment;
import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.RecipeDetailsViewStrategy;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.RecipiesViewStrategy;

import java.io.IOException;
import java.util.List;


public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeDetailsViewStrategy detailsViewStrategy;
    private Recipe selectedRecipe;
    private Boolean isSavedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        extractDataFromBundle();

        if (selectedRecipe == null) {
            Toast.makeText(this, "The recipe is unknown!", Toast.LENGTH_LONG).show();
            return;
        }

        setTitle(selectedRecipe.getTitle());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RecipeDetailsActivity.this, null);
//                RecipeDetailsActivity.this.startActivity(intent);
                ProgressDialog progressDialog = ProgressDialog.show(RecipeDetailsActivity.this,
                        "Save!", "Saving...", true);

                RecipeViewerDatabase database = new RecipeViewerDatabase(RecipeDetailsActivity.this);
                database.open();
                database.addRecipe(selectedRecipe, RecipeDetailsActivity.this);
                database.close();

                progressDialog.cancel();
                Toast.makeText(RecipeDetailsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });

        if (isSavedList) {
            fab.setVisibility(View.GONE);
        }

        TextView ingredientsTextButton = (TextView) findViewById(R.id.ingredientsTextButton);
        TableLayout ingredientsTable = (TableLayout) findViewById(R.id.ingredientsTable);
        CollapsingToolbarLayout recipeDetailsToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.recipe_details_toolbar_layout);
        TextView recipeDescriptionView = (TextView) findViewById(R.id.recipeShortDescription);

        this.detailsViewStrategy = new RecipeDetailsViewStrategy(this,
                ingredientsTable, ingredientsTextButton, recipeDetailsToolbarLayout, recipeDescriptionView);
        detailsViewStrategy.configureIngredientsTextButton();
        detailsViewStrategy.configureRecipeDescriptionAndImageView(selectedRecipe);

        new DownloadIngredientsForRecipeFromService().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractDataFromBundle() {
        int selectedItem = getIntent().getIntExtra(RecipesListFragment.SELECTED_ITEM_KEY, 0);
        this.isSavedList = getIntent().getBooleanExtra(
                RecipesMainActivity.NAVIGATE_TO_SAVED_RECIPES_KEY, false);

        RecipeViewerController controller = RecipeViewerController.Instance();
        List<Recipe> recipes = controller.getRecipes();
        this.selectedRecipe = recipes.get(selectedItem);
    }

    private class DownloadIngredientsForRecipeFromService extends AsyncTask<Void, Void, List<Ingredient>> {

        @Override
        protected List<Ingredient> doInBackground(Void... params) {
            try {
                RecipeViewerController controller = RecipeViewerController.Instance();
                return controller.getIngredientsForRecipeWithId(selectedRecipe.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Ingredient> ingredients) {
            selectedRecipe.setIngredientList(ingredients);
            detailsViewStrategy.configureIngredientsTable(selectedRecipe);
        }
    }
}
