package com.plt3ch.recipeviewer.ViewConfigureStrategies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Models.Recipe;

public class RecipeStepViewStrategy extends ViewStrategy {

    private final ImageView recipeStepImageView;
    private final TextView recipeDescriptionView;

    public RecipeStepViewStrategy(Context context,
                                  ImageView recipeStepImageView,
                                  TextView recipeDescriptionView) {
        super(context);
        this.recipeStepImageView = recipeStepImageView;
        this.recipeDescriptionView = recipeDescriptionView;
    }

    public void configureRecipeStepDescriptionAndImageView(Recipe recipe) {
        if (recipe == null) {
            Toast.makeText(mContext, "No information about the recipe", Toast.LENGTH_LONG).show();
            return;
        }
        if (this.recipeStepImageView == null || this.recipeDescriptionView == null) {
            Toast.makeText(mContext, "There are some issues with the view!", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap recipeImage = recipe.getRecipeImage();
        if (recipeImage != null) {
            Drawable recipeImageDrawable = new BitmapDrawable(mContext.getResources(), recipeImage);
            this.recipeStepImageView.setBackground(recipeImageDrawable);
        }

        if (recipe.getDescription() != null && recipe.getDescription().length() > 0) {
            this.recipeDescriptionView.setText(recipe.getDescription());
        }
    }
}
