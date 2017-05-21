package com.plt3ch.recipeviewer.Controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.plt3ch.recipeviewer.InnerAppModels.RequestState;
import com.plt3ch.recipeviewer.InnerAppModels.WebServiceResponse;
import com.plt3ch.recipeviewer.Models.Ingredient;
import com.plt3ch.recipeviewer.Models.Recipe;
import com.plt3ch.recipeviewer.Models.RegisterUser;
import com.plt3ch.recipeviewer.Models.User;
import com.plt3ch.recipeviewer.Models.UserFeedback;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by plt3ch on 5/7/2015.
 */
class RecipesWebServiceController {

    private static final String TAG = "RVWebController";

    private static final String SERVICE_ADDRESS = "http://192.168.1.104:18888";
//    private static final String SERVICE_ADDRESS = "http://192.168.137.1:18888";
//    private static final String SERVICE_ADDRESS = "http://192.168.43.110:18888";

    private static final String SERVICE_LOGIN_SUFFIX = "/Token";
    private static final String SERVICE_REGISTER_SUFFIX = "/api/Account/Register";
    private static final String SERVICE_ADD_IMAGE_SUFFIX = "/api/UserImages/addImageForUser";
    private static final String SERVICE_ALL_RECIPES_SUFFIX = "/api/Recipe/all";
    private static final String SERVICE_RECIPES_SUFFIX = "/api/Recipe/recipes";
    private static final String USER_INFO_SUFFIX = "/api/Account/UserInfo";
    private static final String SERVICE_INGREDIENTS_FOR_RECIPE_SUFFIX =
            "/api/Ingredients/IngredientsForRecipe/";
    private static final String SERVICE_RECIPE_USERFEEDBACK_SUFFIX = "/api/recipe/AddRecipeComment";

    private static final String HAS_REGISTERED_KEY = "HasRegistered";
    private static final String EMAIL_KEY = "Email";

    public boolean checkIfLoggedUserTokenIsValid() {
        AuthenticationController authController = AuthenticationController.getInstance();
        String username = authController.getLastLoggedUsername();
        String authToken = authController.getLoggedUserAuthToken();

        if (username == null || authToken == null) {
            Log.d(TAG, "When checking authtoken validity, the authToken or username can't be null");
            return false;
        }

        InputStream result = sendGETRequestWithToken(SERVICE_ADDRESS + USER_INFO_SUFFIX, authToken);
        if (result != null) {
            try {
                JSONObject json = transformInputStreamIntoJson(result);
                Boolean hasRegistered = Boolean.parseBoolean(json.getString(HAS_REGISTERED_KEY));
                String email = json.getString(EMAIL_KEY);
                if (hasRegistered && username.equals(email)) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean registerUser(RegisterUser user){
        Gson gson = new Gson();
        String userToString = gson.toJson(user);
        String url = SERVICE_ADDRESS + SERVICE_REGISTER_SUFFIX;
        InputStream result = this.sendJSONToService(userToString, url);

        if (result != null){
            Reader reader = new InputStreamReader(result);
            User returnedUser = gson.fromJson(reader, User.class);
        }

        return false;
    }

    public List<Recipe> getFilteredRecipes(String filterType,
            String filterValue) throws IOException {
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

    public WebServiceResponse performLoginUser(User user) {
        WebServiceResponse webResponse = new WebServiceResponse();
        try {
            HashMap<String, String> postDataParms = new HashMap<>();
            postDataParms.put("grant_type", "password");
            postDataParms.put("username", user.getUsername());
            postDataParms.put("password", user.getPassword());

            String url = SERVICE_ADDRESS + SERVICE_LOGIN_SUFFIX;

            Response response = sendPostUrlEncodedRequestToService(url, postDataParms);
            if(response != null) {
                JSONObject jsonObject = transformInputStreamIntoJson(response.result);
                String userId = jsonObject.getString("userId");
                user.setId(userId);
                webResponse.setResponseValueForKey(WebServiceResponse.AUTH_TOKEN,
                        !jsonObject.isNull("access_token") ?
                                jsonObject.getString("access_token") : null);
            }
        }
        catch (IOException e) {
            webResponse.setState(RequestState.ServiceNotReached);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return webResponse;
    }

    public List<Recipe> getRecipes() throws IOException {
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

    public boolean saveUserComment(UserFeedback feedback) {
        Gson gson = new Gson();
        String feedbackToString = gson.toJson(feedback);
        String url = SERVICE_ADDRESS + SERVICE_RECIPE_USERFEEDBACK_SUFFIX;

        InputStream result = this.sendJSONToService(feedbackToString, url);
        return result != null;
    }

    public List<Ingredient> getIngredientsForRecipeWithId(int id) throws IOException {
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

    private InputStream sendGETRequestWithToken(String url, String authToken) {
        InputStream inResponse = null;
        HttpURLConnection urlConn = null;
        try {
            URL u = new URL(url);
            urlConn = (HttpURLConnection) u.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Authorization", "Bearer " + authToken);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(false);
            urlConn.setConnectTimeout(10000);
            urlConn.setReadTimeout(10000);
            urlConn.connect();
            inResponse = urlConn.getInputStream();
        } catch(IOException e) {
            Log.d("Error",
                    "sendGETRequestWithToken: Failed to retrieve recipes from the web service!");
            e.printStackTrace();
        }

        return inResponse;
    }

    private InputStream sendGetRequestToService(String url) throws IOException {
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
        } catch(IOException e) {
            Log.d("Error",
                    "sendGetRequestToService: Failed to retrieve recipes from the web service!");
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
            urlConn = (HttpURLConnection) u.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConn.connect();
            OutputStream printout = urlConn.getOutputStream();
            printout.write(message.getBytes("UTF-8"));
            printout.flush();
            printout.close();

            inResponse = urlConn.getInputStream();
        } catch(Exception e) {
           e.printStackTrace();
        }

        return inResponse;
    }

    private Response sendPostUrlEncodedRequestToService(String url, HashMap<String, String> postData) throws IOException {
        HttpURLConnection urlConn;
        URL u = new URL (url);
        urlConn =(HttpURLConnection) u.openConnection();
        urlConn.setReadTimeout(15000);
        urlConn.setConnectTimeout(15000);
//            urlConn.setRequestMethod("POST");
        urlConn.setDoOutput(true);
        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);
        urlConn.setAllowUserInteraction(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConn.setRequestProperty("Accept", "*/*");
        urlConn.connect();
        OutputStream os = urlConn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        String dataString = getPostDataString(postData);
        writer.write(dataString);
        writer.flush();
        writer.close();

        int responseCode = urlConn.getResponseCode();
        InputStream inResponse = null;
        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            inResponse = urlConn.getInputStream();
        } else {
            /* error from server */
            inResponse = urlConn.getErrorStream();
        }

        return new Response(responseCode, inResponse);
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

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (!first) {
                result.append("&");
            }

            first = false;
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private JSONObject transformInputStreamIntoJson(InputStream input)
            throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(input, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
        return jsonObject;
    }

    private class Response {
        private int responseCode;
        private InputStream result;

        public Response(int responseCode, InputStream result) {
            this.responseCode = responseCode;
            this.result = result;
        }
    }
}
