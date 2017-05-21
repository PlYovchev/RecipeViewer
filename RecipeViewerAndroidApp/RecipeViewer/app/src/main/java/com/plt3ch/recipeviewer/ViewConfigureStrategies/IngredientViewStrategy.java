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
 * Created by Plamen on 5/15/2017.
 */

public class IngredientViewStrategy extends ViewStrategy {

    private final TableLayout ingredientsTable;
    private final TextView ingredientsTextButton;

    public IngredientViewStrategy(Context context,
                                  TableLayout ingredientsTable,
                                  TextView ingredientsTextButton) {
        super(context);
        this.ingredientsTable = ingredientsTable;
        this.ingredientsTextButton = ingredientsTextButton;
    }

    public void configureIngredientsTable(Recipe recipe) {
        if (this.ingredientsTable == null) {
            Toast.makeText(mContext, "Something went wrong with the view!", Toast.LENGTH_LONG).show();
            return;
        }

        if (recipe == null || recipe.getIngredientList() == null
                || recipe.getIngredientList().size() == 0) {
            Toast.makeText(mContext, "No information about the ingredients", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        for (Ingredient ingredient: recipe.getIngredientList()) {
            TableRow row = (TableRow) LayoutInflater.from(mContext)
                    .inflate(R.layout.ingredient_table_row, null);

            String productName = ingredient.getProduct().getName();
            String quantityFormatted = String.format("%s", ingredient.getQuantity());

            ((TextView)row.findViewById(R.id.productText)).setText(productName);
            ((TextView)row.findViewById(R.id.quantityText)).setText(quantityFormatted);

            this.ingredientsTable.addView(row);
        }
    }

    public void configureIngredientsTextButton() {
        if (this.ingredientsTextButton == null) {
            Toast.makeText(mContext, "Something went wrong with the ingredients expand button!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        this.ingredientsTextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ingredientsTable.isShown()) {
                    slideUp();
                    ingredientsTextButton.setCompoundDrawablesWithIntrinsicBounds(
                            mContext.getResources().getDrawable(android.R.drawable.ic_input_add),
                            null, null, null);
                } else {
                    slideDown();
                    ingredientsTextButton.setCompoundDrawablesWithIntrinsicBounds(
                            mContext.getResources().getDrawable(android.R.drawable.ic_delete),
                            null, null, null);
                }
            }
        });
    }

    private void slideDown() {
        Animation slideDownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        applyAnimationOnIngredientsTable(slideDownAnimation);
    }

    private void slideUp() {
        Animation slideUpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        applyAnimationOnIngredientsTable(slideUpAnimation);
    }

    private void applyAnimationOnIngredientsTable(Animation animation) {
        if (animation != null) {
            animation.setAnimationListener(new IngredientViewStrategy.CustomAnimationListener(this.ingredientsTable));
            animation.reset();
            if (this.ingredientsTable != null) {
                this.ingredientsTable.clearAnimation();
                this.ingredientsTable.startAnimation(animation);
            }
        }
    }

    private static class CustomAnimationListener implements Animation.AnimationListener {

        TableLayout tableLayout;
        boolean isSlidedDown;

        public CustomAnimationListener(TableLayout tableLayout) {
            this.tableLayout = tableLayout;
            this.isSlidedDown = tableLayout.isShown();
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (!tableLayout.isShown()) {
                isSlidedDown = false;
                tableLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (tableLayout.isShown()) {
                if (isSlidedDown) {
                    tableLayout.setVisibility(View.GONE);
                } else {
                    isSlidedDown = true;
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
