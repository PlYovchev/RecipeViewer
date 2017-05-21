package com.plt3ch.recipeviewer.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.plt3ch.recipeviewer.Models.UserFeedback;
import com.plt3ch.recipeviewer.R;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.IngredientViewStrategy;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.RecipeDetailsViewStrategy;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.RecipiesViewStrategy;

import java.io.IOException;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeDetailsViewStrategy detailsViewStrategy;
    private IngredientViewStrategy ingredientssViewStrategy;
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
            RecipeViewerController controller = RecipeViewerController.Instance();
            List<Recipe> recipes = controller.getRecipes();

            Intent intent = new Intent(RecipeDetailsActivity.this, RecipeStepsActivity.class);
            intent.putExtra(RecipesListFragment.SELECTED_ITEM_KEY, recipes.indexOf(selectedRecipe));
            RecipeDetailsActivity.this.startActivity(intent);
            }
        });

//        if (this.isSavedList) {
//            fab.setVisibility(View.GONE);
//        }

        TextView ingredientsTextButton = (TextView) findViewById(R.id.ingredientsTextButton);
        TableLayout ingredientsTable = (TableLayout) findViewById(R.id.ingredientsTable);
        CollapsingToolbarLayout recipeDetailsToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.recipe_details_toolbar_layout);
        TextView recipeDescriptionView = (TextView) findViewById(R.id.recipeShortDescription);

        this.ingredientssViewStrategy = new IngredientViewStrategy(this,
                ingredientsTable, ingredientsTextButton);
        this.ingredientssViewStrategy.configureIngredientsTextButton();

        this.detailsViewStrategy = new RecipeDetailsViewStrategy(this,
                recipeDetailsToolbarLayout, recipeDescriptionView);
        this.detailsViewStrategy.configureRecipeDescriptionAndImageView(selectedRecipe);

        new DownloadIngredientsForRecipeFromService().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe_details, menu);
        if (this.isSavedList) {
            MenuItem saveActionItem = menu.findItem(R.id.action_save);
            saveActionItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_comment:
                showAddCommentDialog();
                return true;
            case R.id.action_save:
                saveRecipeDetails();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRecipeDetails() {
        ProgressDialog progressDialog = ProgressDialog.show(RecipeDetailsActivity.this,
                "Save!", "Saving...", true);

        RecipeViewerDatabase database = new RecipeViewerDatabase(RecipeDetailsActivity.this);
        database.open();
        database.addRecipe(selectedRecipe, RecipeDetailsActivity.this);
        database.close();

        progressDialog.cancel();
        Toast.makeText(RecipeDetailsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    private void showAddCommentDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_comment_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText commentText = (EditText) dialogView.findViewById(R.id.commentText);
                        RatingBar userRecipeRating = (RatingBar) dialogView
                                .findViewById(R.id.userRecipeRating);

                        final UserFeedback feedback = new UserFeedback();
                        feedback.setComment(commentText.getText().toString());
                        feedback.setRating(Math.round(userRecipeRating.getRating()));
                        feedback.setRecipeId(selectedRecipe.getId());
                        feedback.setUserId(RecipeViewerController.Instance().getLoggedUser().getId());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RecipeViewerController.Instance().saveUserFeedback(feedback);
                            }
                        }).start();
                    }
                });
        dialogBuilder.create().show();
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
            ingredientssViewStrategy.configureIngredientsTable(selectedRecipe);
        }
    }
}