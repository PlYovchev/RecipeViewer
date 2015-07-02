package com.plt3ch.recipeviewer.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.plt3ch.recipeviewer.Adapters.IngredientsAdapter;
import com.plt3ch.recipeviewer.Adapters.RecipesAdapter;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.List;

public class RecipeDetailsFragment extends Fragment {

    private Recipe selectedRecipe;
    private ListView ingredientsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        int selectedItem = args.getInt(RecipesListFragment.SELECTED_ITEM_KEY);

        RecipeViewerController controller = RecipeViewerController.Instance();
        List<Recipe> recipes = controller.getRecipes();
        this.selectedRecipe = recipes.get(selectedItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        TextView titleTextView = (TextView) contentView.findViewById(R.id.detailsTitleTextView);
        ImageView imageView = (ImageView) contentView.findViewById(R.id.detailsImageView);
        TextView authorTextView = (TextView) contentView.findViewById(R.id.detailsAuthorTextView);
        TextView descriptionTextView = (TextView) contentView.findViewById(R.id.detailsDescriptionTextView);
        this.ingredientsListView = (ListView) contentView.findViewById(R.id.detailsIngredientslistView);

        titleTextView.setText(this.selectedRecipe.getTitle());
        imageView.setImageBitmap(this.selectedRecipe.getRecipeImage());
        authorTextView.setText(this.selectedRecipe.getAuthorUserName());
        descriptionTextView.setText(this.selectedRecipe.getDescription());

        setHasOptionsMenu(true);

        new DownloadIngredientsForRecipeFromService().execute();

        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class DownloadIngredientsForRecipeFromService extends AsyncTask<Void, Void, List<Ingredient>> {

        @Override
        protected List<Ingredient> doInBackground(Void... params) {
            //Bitmap recipeBitmap = HelpersImageDecodeders.decodeSampledBitmapFromResource(getResources(),R.drawable.pizza, 500, 500);

            RecipeViewerController controller = RecipeViewerController.Instance();
            List<Ingredient> ingredients = controller.getIngredientsForRecipeWithId(selectedRecipe.getId());

            return ingredients;
        }

        @Override
        protected void onPostExecute(List<Ingredient> ingredients) {
            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getActivity(), ingredients);
            RecipeDetailsFragment.this.ingredientsListView.setAdapter(ingredientsAdapter);
            RecipeDetailsFragment.setListViewHeightBasedOnChildren(RecipeDetailsFragment.this.ingredientsListView);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
