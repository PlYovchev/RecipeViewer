package com.plt3ch.recipeviewer.Controllers;

import android.graphics.Bitmap;

import com.plt3ch.recipeviewer.FilterByType;
import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.Models.RegisterUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by plt3ch on 5/7/2015.
 */
public class RecipeViewerController {

    private List<Recipe> recipes;
    private Recipe chosenRecipe;
    private HashMap<String, ArrayList<String>> filterDictionary;
    private FilterByType filterByType;
    private String lastFilterValue;

    private static RecipeViewerController recipeViewerController;

    private RecipeViewerController(){
        this.filterByType = FilterByType.Title;
    }

    public static RecipeViewerController Instance(){

        if(recipeViewerController == null){
            recipeViewerController = new RecipeViewerController();
        }

        return recipeViewerController;
    }

    public Bitmap downloadImageByUrl(String url) {
        RecipesWebServiceController recipesWebServiceController = new RecipesWebServiceController();
        return recipesWebServiceController.downloadImageByUrl(url);
    }

    public void registerUser(RegisterUser user){
        RecipesWebServiceController recipesWebServiceController = new RecipesWebServiceController();
        recipesWebServiceController.registerUser(user);
    }

    public void addImage(Bitmap bitmap){
        RecipesWebServiceController recipesWebServiceController = new RecipesWebServiceController();
        recipesWebServiceController.addImage(bitmap);
    }

    public void getRecipesFilterByTypeWithValue(String value){
        RecipesWebServiceController recipesWebServiceController = new RecipesWebServiceController();
        this.recipes = recipesWebServiceController.getFilteredRecipes(this.filterByType.toString(), value);
        setLastFilterValue(value);
    }


    public List<Recipe> getRecipes(){
        return this.recipes;
    }

    public List<Ingredient> getIngredientsForRecipeWithId(int id) {
        RecipesWebServiceController recipesWebServiceController = new RecipesWebServiceController();
        return recipesWebServiceController.getIngredientsForRecipeWithId(id);
    }

    public boolean getAllRecipesFromService(){
        RecipesWebServiceController recipesWebServiceController = new RecipesWebServiceController();
        this.recipes = recipesWebServiceController.getRecipes();

        if(this.recipes != null){
            return true;
        }else{
            return false;
        }
    }

    public Recipe getChosenRecipe() {
        return chosenRecipe;
    }

    public void setChosenRecipe(Recipe chosenRecipe) {
        this.chosenRecipe = chosenRecipe;
    }

    public HashMap<String, ArrayList<String>> getFilterDictionary() {
        if(filterDictionary == null){
            this.filterDictionary = new HashMap<String, ArrayList<String>>();
        }

        return filterDictionary;
    }

    public void setFilterDictionary(HashMap<String, ArrayList<String>> filterDictionary) {
        this.filterDictionary = filterDictionary;
    }

    public FilterByType getFilterByType() {
        return filterByType;
    }

    public void setFilterByType(FilterByType filterByType) {
        this.filterByType = filterByType;
    }

    public String getLastFilterValue() {
        return lastFilterValue;
    }

    public void setLastFilterValue(String lastFilterValue) {
        this.lastFilterValue = lastFilterValue;
    }
}
