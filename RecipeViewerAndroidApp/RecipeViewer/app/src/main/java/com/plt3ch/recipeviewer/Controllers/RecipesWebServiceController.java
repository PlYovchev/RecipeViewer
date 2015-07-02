package com.plt3ch.recipeviewer.Controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.plt3ch.recipeviewer.HelpersImageDecodeders;
import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.Models.RegisterUser;
import com.plt3ch.recipeviewer.Models.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by plt3ch on 5/7/2015.
 */
class RecipesWebServiceController {

    private static final String SERVICE_ADDRESS = "http://192.168.1.104:18888/api";
//    private static final String SERVICE_ADDRESS = "http://192.168.137.1:18888/api";
    private static final String SERVICE_LOGIN_SUFFIX = "/Token";
    private static final String SERVICE_REGISTER_SUFFIX = "/Account/Register";
    private static final String SERVICE_ADD_IMAGE_SUFFIX = "/UserImages/addImageForUser";
    private static final String SERVICE_ALL_RECIPES_SUFFIX = "/Recipe/all";
    private static final String SERVICE_RECIPES_SUFFIX = "/Recipe/recipes";
    private static final String SERVICE_INGREDIENTS_FOR_RECIPE_SUFFIX = "/Recipe/IngredientsForRecipe/";


    public boolean registerUser(RegisterUser user){
        Gson gson = new Gson();
        String userToString = gson.toJson(user);
        String url = SERVICE_ADDRESS + SERVICE_REGISTER_SUFFIX;
        InputStream result = this.sendJSONToService(userToString, url);

        if(result != null){
            Reader reader = new InputStreamReader(result);
            User returnedUser = gson.fromJson(reader, User.class);
        }

        return false;
    }

    public List<Recipe> getFilteredRecipes(String filterType, String filterValue){
        List<Recipe> recipes = null;
        String url = SERVICE_ADDRESS + SERVICE_RECIPES_SUFFIX + "?filterType=" + filterType + "&" + "filterValue=" + filterValue;

        InputStream result = sendGetRequestToService(url);
        if(result != null){
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Type listOfRecipes = new TypeToken<List<Recipe>>(){}.getType();
            Reader reader = new InputStreamReader(result);
            recipes = gson.fromJson(reader, listOfRecipes);
        }

        return recipes;
    }

    public boolean validateLoginUser(User user){
        return false;
    }

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = null;
        String url = SERVICE_ADDRESS + SERVICE_ALL_RECIPES_SUFFIX;

        InputStream result = sendGetRequestToService(url);
        if(result != null){
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Type listOfRecipes = new TypeToken<List<Recipe>>(){}.getType();
            Reader reader = new InputStreamReader(result);
            recipes = gson.fromJson(reader, listOfRecipes);
        }

        return recipes;
    }

    public List<Ingredient> getIngredientsForRecipeWithId(int id){
        List<Ingredient> ingredients = null;
        String url = SERVICE_ADDRESS + SERVICE_INGREDIENTS_FOR_RECIPE_SUFFIX + id;

        InputStream result = sendGetRequestToService(url);
        if(result != null){
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Type listOfIngredients = new TypeToken<List<Ingredient>>(){}.getType();
            Reader reader = new InputStreamReader(result);
            ingredients = gson.fromJson(reader, listOfIngredients);
        }

        return ingredients;
    }

    public List<Recipe> getRecipesByUser(User user){

        return null;
    }

    public boolean addImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        String url = SERVICE_ADDRESS + SERVICE_ADD_IMAGE_SUFFIX;
        InputStream result = this.sendImageAsBytesToService("dashdjkafasjhfkajhfaskjhfajkshfajk", url, bitmapdata);

        return true;
    }

    public Bitmap downloadImageByUrl(String url){
        try{
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is= conn.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            Bitmap image = BitmapFactory.decodeStream(bufferedInputStream);
            return image;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


    private InputStream sendGetRequestToService(String url){
        InputStream inResponse = null;
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);
            c.connect();
            inResponse = c.getInputStream();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return inResponse;
    }

    private InputStream sendJSONToService(String message, String url){
        InputStream inResponse = null;
        try {
            HttpURLConnection urlConn;
            DataInputStream input;
            URL u = new URL (url);
            urlConn =(HttpURLConnection) u.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConn.connect();
            OutputStream printout= urlConn.getOutputStream();
            printout.write(message.getBytes("UTF-8"));
            printout.flush();
            printout.close();

            inResponse = urlConn.getInputStream();
        } catch(Exception e) {
           e.printStackTrace();
        }

        return inResponse;
    }

    private InputStream sendImageAsBytesToService(String message, String url, byte[] content){
        InputStream inResponse = null;
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;

        try {
            HttpPost post = new HttpPost(url);

            post.setEntity(new ByteArrayEntity(content));
            response = client.execute(post);

            /*Checking response */
            if(response!=null){
                inResponse = response.getEntity().getContent(); //Get the data in the entity
                Log.d("RecipeViewer", "Image not send");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return inResponse;
    }

}
