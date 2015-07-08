package com.plt3ch.recipeviewer.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plt3ch.recipeviewer.Activities.RecipesMainActivity;
import com.plt3ch.recipeviewer.Adapters.IngredientsAdapter;
import com.plt3ch.recipeviewer.Adapters.RecipesAdapter;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerDatabase;
import com.plt3ch.recipeviewer.Dialogs.ConfigureSearchDialog;
import com.plt3ch.recipeviewer.FilterByType;
import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.List;

public class RecipeDetailsFragment extends Fragment {

    private Recipe selectedRecipe;
    private ListView ingredientsListView;
    private Boolean isSavedList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        int selectedItem = args.getInt(RecipesListFragment.SELECTED_ITEM_KEY);
        this.isSavedList = args.getBoolean(RecipesMainActivity.NAVIGATE_TO_SAVED_RECIPES_KEY);

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
        if(this.isSavedList) {
            MenuItem item = menu.findItem(R.id.action_save);
            item.setVisible(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        RecipeViewerController controller = RecipeViewerController.Instance();
        switch (id){
        case R.id.action_save:
            ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Save!", "Saving...", true);

            RecipeViewerDatabase database = new RecipeViewerDatabase(this.getActivity());
            database.open();
            database.addRecipe(this.selectedRecipe, this.getActivity());
            database.close();

            progressDialog.cancel();
            Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadIngredientsForRecipeFromService extends AsyncTask<Void, Void, List<Ingredient>> {

        @Override
        protected List<Ingredient> doInBackground(Void... params) {
            RecipeViewerController controller = RecipeViewerController.Instance();
            return controller.getIngredientsForRecipeWithId(selectedRecipe.getId());
        }

        @Override
        protected void onPostExecute(List<Ingredient> ingredients) {
            selectedRecipe.setIngredientList(ingredients);
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
