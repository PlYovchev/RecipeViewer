package com.plt3ch.recipeviewer.Fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.plt3ch.recipeviewer.Activities.RecipesMainActivity;
import com.plt3ch.recipeviewer.Adapters.RecipesAdapter;
import com.plt3ch.recipeviewer.Controllers.RecipeViewerController;
import com.plt3ch.recipeviewer.Dialogs.ConfigureSearchDialog;
import com.plt3ch.recipeviewer.FilterByType;
import com.plt3ch.recipeviewer.HelpersImageDecodeders;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.R;

import java.util.ArrayList;
import java.util.List;
/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * interface.
 */
public class RecipesListFragment extends ListFragment {

    private Handler handler;
    private Runnable onSearchRunnble;

    private String filterValue;

    public static final String SELECTED_ITEM_KEY = "selectedItem";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        new DownloadRecipesFromService().execute("");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setSelector(R.drawable.list_selector);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipes_main, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeViewerController controller = RecipeViewerController.Instance();
                searchView.setQuery(controller.getLastFilterValue(), false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterValue = newText;

                if(handler == null){
                    handler = new Handler(getActivity().getMainLooper());
                }

                if(onSearchRunnble == null){
                    onSearchRunnble = new Runnable() {
                        @Override
                        public void run() {
                            Log.d("RV",filterValue);
                            new DownloadRecipesFromService().execute(filterValue);
                        }
                    };
                }

                handler.removeCallbacks(onSearchRunnble);
                handler.postDelayed(onSearchRunnble, 2000);

                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        RecipeViewerController controller = RecipeViewerController.Instance();
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_configure_search:{
                ConfigureSearchDialog configureSearchDialog = new ConfigureSearchDialog();

                FragmentManager fragmentManager = getFragmentManager();
                configureSearchDialog.show(fragmentManager, "configureSearchDialog");
                return true;
            }
            case R.id.action_filterby_author:
                controller.setFilterByType(FilterByType.Author);
                return true;
            case R.id.action_filterby_title:
                controller.setFilterByType(FilterByType.Title);
                return true;
            case R.id.action_filterby_ingredient:
                controller.setFilterByType(FilterByType.Ingredient);
                return true;
            case R.id.action_filterby_rating:
                controller.setFilterByType(FilterByType.Rating);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(SELECTED_ITEM_KEY, position);
        recipeDetailsFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, recipeDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private class DownloadRecipesFromService extends AsyncTask <String, Void, List<Recipe>> {

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            RecipesAdapter recipesAdapter = new RecipesAdapter(getActivity(), recipes);
            setListAdapter(recipesAdapter);
        }

        @Override
        protected List<Recipe> doInBackground(String... params) {
            //Bitmap recipeBitmap = HelpersImageDecodeders.decodeSampledBitmapFromResource(getResources(),R.drawable.pizza, 500, 500);
            String filterValue = params[0];

            RecipeViewerController controller = RecipeViewerController.Instance();
            controller.getRecipesFilterByTypeWithValue(filterValue);
            List<Recipe> recipes = controller.getRecipes();
            for (Recipe recipe : recipes){
                recipe.setRecipeImage(controller.downloadImageByUrl(recipe.getImageUrl()));
            }

            return recipes;
        }
    }
}
