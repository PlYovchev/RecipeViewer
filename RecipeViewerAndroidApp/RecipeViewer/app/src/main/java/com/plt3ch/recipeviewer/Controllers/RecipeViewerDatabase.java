package com.plt3ch.recipeviewer.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Product;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.Models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by plt3ch on 7/7/2015.
 */
public class RecipeViewerDatabase {
    private SQLiteDatabase database;
    private RecipeViewerSqliteHelper recipeViewerDbHelper;

    private static final String IMAGE_NAME_BASE = "imageForRecipeWithId";

    private class RecipeViewerSqliteHelper extends SQLiteOpenHelper{
        private static final String TABLE_RECIPES = "recipes";
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_TITLE = "title";
        private static final String COLUMN_AUTHOR = "author";
        private static final String COLUMN_DESCRIPTION = "description";
        private static final String COLUMN_RATING = "rating";
        private static final String COLUMN_USERID = "userid";
        private static final String COLUMN_IMAGE_PATH = "imagepath";

        private static final String TABLE_INGREDIENTS = "ingredients";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_QUANTITY = "quantity";
        private static final String COLUMN_RECIPEID = "recipeid";

        private static final String DATABASE_NAME = "recipeviewer.db";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE_TABLE_RECIPES = "create table " +
                TABLE_RECIPES + "(" + COLUMN_ID + " integer primary key, " + COLUMN_TITLE + " text, " +
                COLUMN_AUTHOR + " text," + COLUMN_DESCRIPTION + " text, " + COLUMN_RATING + " integer, " + COLUMN_IMAGE_PATH + " text, " + COLUMN_USERID + " text not null);";
        private static final String DATABASE_CREATE_TABLE_INGREDIENTS = "create table " +
                TABLE_INGREDIENTS + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_NAME + " text, " +
                COLUMN_QUANTITY + " text, " + COLUMN_RECIPEID + " integer not null);";

        public RecipeViewerSqliteHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TABLE_RECIPES);
            db.execSQL(DATABASE_CREATE_TABLE_INGREDIENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(RecipeViewerSqliteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
            onCreate(db);
        }
    }
    
    public RecipeViewerDatabase(Context context){
        this.recipeViewerDbHelper = new RecipeViewerSqliteHelper(context);
    }

    public void open() throws SQLException{
        this.database = this.recipeViewerDbHelper.getWritableDatabase();
    }

    public void close() {
        this.recipeViewerDbHelper.close();
        this.database.close();
    }

    public void addRecipe(Recipe recipe, Context context){
        RecipeViewerController controller = RecipeViewerController.Instance();

        ContentValues valuesRecipe = new ContentValues();
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_ID, recipe.getId());
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_AUTHOR, recipe.getAuthorUserName());
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_TITLE, recipe.getTitle());
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_DESCRIPTION, recipe.getDescription());
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_RATING, recipe.getRating());
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_USERID, controller.getLoggedUser().getId());

        String path = this.saveToInternalSorage(recipe.getRecipeImage(), context, IMAGE_NAME_BASE + recipe.getId());
        valuesRecipe.put(RecipeViewerSqliteHelper.COLUMN_IMAGE_PATH, path);

        long insertRecipeId = database.insert(RecipeViewerSqliteHelper.TABLE_RECIPES, null, valuesRecipe);

        for (Ingredient ingredient : recipe.getIngredientList()) {
            ContentValues valuesIngredients = new ContentValues();
            valuesIngredients.put(RecipeViewerSqliteHelper.COLUMN_NAME, ingredient.getProduct().getName());
            valuesIngredients.put(RecipeViewerSqliteHelper.COLUMN_QUANTITY, ingredient.getQuantity());
            valuesIngredients.put(RecipeViewerSqliteHelper.COLUMN_RECIPEID, recipe.getId());

            long insertIngredientId = database.insert(RecipeViewerSqliteHelper.TABLE_INGREDIENTS, null, valuesIngredients);
        }
    }

    public List<Recipe> getSavedRecipeForCurrentUser(){
        RecipeViewerController controller = RecipeViewerController.Instance();
        User user = controller.getLoggedUser();
        List<Recipe> recipes = new ArrayList<>();

        Cursor cursor = database.query(RecipeViewerSqliteHelper.TABLE_RECIPES,
                null, RecipeViewerSqliteHelper.COLUMN_USERID + " like '" + user.getId() + "'", null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursorToRecipe(cursor);
            recipes.add(recipe);
            cursor.moveToNext();
        }

        cursor.close();
        return recipes;
    }

    private Recipe cursorToRecipe(Cursor cursor){
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getInt(0));
        recipe.setTitle(cursor.getString(1));
        recipe.setAuthorUserName(cursor.getString(2));
        recipe.setDescription(cursor.getString(3));
        recipe.setRating(cursor.getInt(4));
        String path = cursor.getString(5);
        recipe.setRecipeImage(loadImageFromStorage(path));

        Cursor cursorIngredients = database.query(RecipeViewerSqliteHelper.TABLE_INGREDIENTS,
                null, RecipeViewerSqliteHelper.COLUMN_RECIPEID + " = " + recipe.getId(), null,
                null, null, null);
        cursorIngredients.moveToFirst();
        while (!cursorIngredients.isAfterLast()) {
            Ingredient ingredient = cursorToIngredient(cursorIngredients);
            recipe.getIngredientList().add(ingredient);
            cursorIngredients.moveToNext();
        }

        cursorIngredients.close();
        return recipe;
    }

    private Ingredient cursorToIngredient(Cursor cursor){
        Ingredient ingredient = new Ingredient();
        Product product = new Product();
        product.setName(cursor.getString(1));
        ingredient.setProduct(product);
        ingredient.setQuantity(cursor.getString(2));

        return ingredient;
    }

    private String saveToInternalSorage(Bitmap bitmapImage, Context context, String filename){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, filename+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mypath.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path)
    {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
