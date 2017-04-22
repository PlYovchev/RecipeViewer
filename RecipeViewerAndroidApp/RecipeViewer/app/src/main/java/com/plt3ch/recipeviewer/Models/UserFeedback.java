package com.plt3ch.recipeviewer.Models;

import java.io.Serializable;

/**
 * Created by Plamen on 2/13/2017.
 */

public class UserFeedback implements Serializable {

    private String UserId;
    private int RecipeId;
    private String Comment;
    private int Rating;

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(int recipeId) {
        RecipeId = recipeId;
    }
}
