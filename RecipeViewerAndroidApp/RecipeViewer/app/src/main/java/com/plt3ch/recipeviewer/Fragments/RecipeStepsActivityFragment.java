package com.plt3ch.recipeviewer.Fragments;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.IngredientViewStrategy;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.RecipeDetailsViewStrategy;
import com.plt3ch.recipeviewer.ViewConfigureStrategies.RecipeStepViewStrategy;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepsActivityFragment extends Fragment {

    private int numberOfStep;
    private Recipe selectedRecipe;

    private IngredientViewStrategy ingredientsViewStrategy;
    private RecipeStepViewStrategy recipeStepViewStrategy;

    public RecipeStepsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        String stepFormatter = contentView.getResources().getString(R.string.stepLabelFormatter);
        TextView stepLabel = (TextView) contentView.findViewById(R.id.stepNumberText);
        stepLabel.setText(String.format(stepFormatter, numberOfStep));

        TextView ingredientsTextButton = (TextView) contentView.findViewById(R.id.ingredientsTextButton);
        TableLayout ingredientsTable = (TableLayout) contentView.findViewById(R.id.ingredientsTable);
        ImageView stepImage = (ImageView) contentView.findViewById(R.id.stepImage);
        CollapsingToolbarLayout recipeDetailsToolbarLayout =
                (CollapsingToolbarLayout) contentView.findViewById(R.id.recipe_details_toolbar_layout);
        TextView recipeDescriptionView = (TextView) contentView.findViewById(R.id.stepDescription);

        this.ingredientsViewStrategy = new IngredientViewStrategy(getActivity(),
                ingredientsTable, ingredientsTextButton);
        this.ingredientsViewStrategy.configureIngredientsTextButton();

        this.recipeStepViewStrategy = new RecipeStepViewStrategy(getActivity(), stepImage,
                recipeDescriptionView);

        if (this.selectedRecipe != null) {
            this.ingredientsViewStrategy.configureIngredientsTable(this.selectedRecipe);
            recipeStepViewStrategy.configureRecipeStepDescriptionAndImageView(this.selectedRecipe);
        }

        return contentView;
    }

    public int getNumberOfStep() {
        return numberOfStep;
    }

    public void setNumberOfStep(int numberOfStep) {
        this.numberOfStep = numberOfStep;
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }
}
