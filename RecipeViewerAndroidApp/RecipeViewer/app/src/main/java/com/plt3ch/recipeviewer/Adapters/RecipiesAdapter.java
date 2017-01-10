package com.plt3ch.recipeviewer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.List;

/**
 * Created by Plamen on 1/9/2017.
 */

public class RecipiesAdapter extends ArrayAdapter<Recipe> {

    private List<Recipe> recipies;

    public RecipiesAdapter(List<Recipe> recipies, Context context) {
        super(context, R.layout.recipe_item_row, recipies);
        this.recipies = recipies;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recipe_item_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.recipeImageView);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameText);
            viewHolder.categoryView = (TextView) convertView.findViewById(R.id.categoryText);
            viewHolder.durationView = (TextView) convertView.findViewById(R.id.durationText);
            viewHolder.difficultyView = (TextView) convertView.findViewById(R.id.difficultyText);
            viewHolder.ratingView = (RatingBar) convertView.findViewById(R.id.ratingBar);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.nameView.setText(recipe.getTitle());
        viewHolder.categoryView.setText(recipe.getCategory().getFriendlyName());
        viewHolder.difficultyView.setText(recipe.getDifficulty().name());
        viewHolder.durationView.setText(String.format("%s %s", recipe.getDuration(),
                getContext().getResources().getString(R.string.time_unit)));
        viewHolder.ratingView.setRating(recipe.getRating());

        return result;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView categoryView;
        TextView durationView;
        TextView difficultyView;
        RatingBar ratingView;
    }
}
