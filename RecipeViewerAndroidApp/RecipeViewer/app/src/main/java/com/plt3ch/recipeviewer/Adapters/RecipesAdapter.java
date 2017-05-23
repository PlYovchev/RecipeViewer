package com.plt3ch.recipeviewer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.List;

/**
 * Created by plt3ch on 5/9/2015.
 */
public class RecipesAdapter extends BaseAdapter{

    private Context context;
    private List<Recipe> recipesList;

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipesList = recipes;
    }

    @Override
    public int getCount() {
        return this.recipesList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.recipesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.recipe_list_item, null);
        }

        ImageView recipeImage = (ImageView) convertView.findViewById(R.id.recipeImageView);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView txtAuthor = (TextView) convertView.findViewById(R.id.authorTextView);
        RatingBar recipeRatingBar = (RatingBar) convertView.findViewById(R.id.recipeRatingBar);

        Recipe currentRecipe = recipesList.get(position);

        recipeImage.setImageBitmap(currentRecipe.getScaledRecipeImage());
        txtTitle.setText(currentRecipe.getTitle());
        String authorName = currentRecipe.getAuthorUserName() != null
                ? currentRecipe.getAuthorUserName() : "Anonymous";
        txtAuthor.setText("by " + authorName);
        recipeRatingBar.setRating(currentRecipe.getRating());

        return convertView;
    }
}
