package com.plt3ch.recipeviewer.Models;

import com.plt3ch.recipeviewer.Models.Enums.ProductCategory;

import java.io.Serializable;

/**
 * Created by Plamen on 1/9/2017.
 */

public class Product implements Serializable {
    private int Id;
    private String Name;
    private int Category;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ProductCategory getCategory() {
        return ProductCategory.values()[Category];
    }

    public void setCategory(int category) {
        this.Category = category;
    }
}
