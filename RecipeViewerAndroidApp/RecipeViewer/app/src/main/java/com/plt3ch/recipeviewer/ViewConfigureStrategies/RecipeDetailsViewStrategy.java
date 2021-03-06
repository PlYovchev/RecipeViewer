package com.plt3ch.recipeviewer.ViewConfigureStrategies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

/**
 * Created by Plamen on 1/9/2017.
 */

public class RecipeDetailsViewStrategy extends ViewStrategy {

    private final CollapsingToolbarLayout recipeDetailsToolbar;
    private final TextView recipeDescriptionView;

    public RecipeDetailsViewStrategy(Context context,
                                     CollapsingToolbarLayout recipeDetailsToolbar,
                                     TextView recipeDescriptionView) {
        super(context);
        this.recipeDetailsToolbar = recipeDetailsToolbar;
        this.recipeDescriptionView = recipeDescriptionView;
    }

    public void configureRecipeDescriptionAndImageView(Recipe recipe) {
        if (recipe == null) {
            Toast.makeText(mContext, "No information about the recipe", Toast.LENGTH_LONG).show();
            return;
        }
        if (this.recipeDetailsToolbar == null || this.recipeDescriptionView == null) {
            Toast.makeText(mContext, "There are some issues with the view!", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap recipeImage = recipe.getRecipeImage();
        if (recipeImage != null) {
            Drawable recipeImageDrawable = new BitmapDrawable(mContext.getResources(), recipeImage);
            this.recipeDetailsToolbar.setBackground(recipeImageDrawable);
        }

        if (recipe.getDescription() != null && recipe.getDescription().length() > 0) {
            this.recipeDescriptionView.setText(recipe.getDescription());
        }
    }
}
