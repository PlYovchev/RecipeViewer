package com.plt3ch.recipeviewer.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Fragments.RecipeStepsActivityFragment;
import com.plt3ch.recipeviewer.Fragments.RecipesListFragment;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity {

    private int currentStepNumber = 0;

    private Recipe selectedRecipe;

    private FloatingActionButton fabForward;
    private FloatingActionButton fabBackward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extractDataFromBundle();

        getSupportActionBar().setTitle(this.selectedRecipe.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabForward = (FloatingActionButton) findViewById(R.id.fabForward);
        fabForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStepWorkflow();
            }
        });

        fabBackward = (FloatingActionButton) findViewById(R.id.fabBackward);
        fabBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                currentStepNumber--;
                manageFabButtonsVisibility();
            }
        });

        nextStepWorkflow();
    }

    private void nextStepWorkflow() {
        currentStepNumber++;
        boolean addToBackStack = currentStepNumber > 1;
        invokeRecipeStepFragment(addToBackStack, currentStepNumber);
        manageFabButtonsVisibility();
    }

    private void invokeRecipeStepFragment(boolean addToBackStack, int stepNumber) {
        RecipeStepsActivityFragment newFragment = new RecipeStepsActivityFragment();
        newFragment.setNumberOfStep(stepNumber);
        newFragment.setSelectedRecipe(this.selectedRecipe);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.stepFragment, newFragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();
    }

    private void manageFabButtonsVisibility() {
        if (currentStepNumber <= 1) {
            fabBackward.setVisibility(View.INVISIBLE);
        } else {
            fabBackward.setVisibility(View.VISIBLE);
        }
    }

    private void extractDataFromBundle() {
        int selectedItemIndex = getIntent().getIntExtra(RecipesListFragment.SELECTED_ITEM_KEY, 0);
        if (selectedItemIndex < 0) {
            return;
        }

        RecipeViewerController controller = RecipeViewerController.Instance();
        List<Recipe> recipes = controller.getRecipes();
        this.selectedRecipe = recipes.get(selectedItemIndex);
    }

}
