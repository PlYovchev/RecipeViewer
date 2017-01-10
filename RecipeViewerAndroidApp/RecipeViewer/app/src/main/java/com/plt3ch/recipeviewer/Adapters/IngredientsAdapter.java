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

import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.List;

/**
 * Created by plt3ch on 6/16/2015.
 */
public class IngredientsAdapter extends BaseAdapter{

    private Context context;
    private List<Ingredient> ingredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public int getCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.ingredients.get(position);
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
            convertView = mInflater.inflate(R.layout.ingredient_list_item, null);
        }

        TextView ingredientTextView = (TextView) convertView.findViewById(R.id.ingredientTextView);
        TextView quantityTextView = (TextView) convertView.findViewById(R.id.quantityTextView);

        Ingredient currentIngredient = this.ingredients.get(position);

        ingredientTextView.setText(currentIngredient.getProduct().getName());
        quantityTextView.setText(currentIngredient.getQuantity());

        return convertView;
    }
}
