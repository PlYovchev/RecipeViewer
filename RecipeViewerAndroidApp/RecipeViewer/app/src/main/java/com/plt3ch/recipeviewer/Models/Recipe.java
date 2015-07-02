package com.plt3ch.recipeviewer.Models;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by plt3ch on 5/7/2015.
 */
public class Recipe {
    private int Id;
    private String Title;
    private String AuthorUserName;
    private int Rating;
    private String Description;
    private Date DateRecipeAdded;
    private String ImageUrl;
    private Bitmap recipeImage;
    private Bitmap scaledRecipeImage;

    public Recipe(){}

    public Recipe(int id, String title, Bitmap recipeImage, int rating, String imageUrl, String description, Date dateRecipeAdded, String authorUserName) {
        this.Id = id;
        this.Title = title;
        this.recipeImage = recipeImage;
        this.Rating = rating;
        this.ImageUrl = imageUrl;
        this.Description = description;
        this.DateRecipeAdded = dateRecipeAdded;
        this.AuthorUserName = authorUserName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getAuthorUserName() {
        return AuthorUserName;
    }

    public void setAuthorUserName(String authorUserName) {
        this.AuthorUserName = authorUserName;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        this.Rating = rating;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public Date getDateRecipeAdded() {
        return DateRecipeAdded;
    }

    public void setDateRecipeAdded(Date dateRecipeAdded) {
        this.DateRecipeAdded = dateRecipeAdded;
    }

    public Bitmap getRecipeImage() {
        return recipeImage;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public void setRecipeImage(Bitmap recipeImage) {
        if(recipeImage != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(recipeImage, 120, 120, false);
            this.recipeImage = recipeImage;
            this.scaledRecipeImage = scaledBitmap;
        }
    }

    public Bitmap getScaledRecipeImage() {
        return scaledRecipeImage;
    }
}
