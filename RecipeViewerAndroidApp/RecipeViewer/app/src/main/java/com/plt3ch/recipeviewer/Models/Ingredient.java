package com.plt3ch.recipeviewer.Models;

import java.io.Serializable;

/**
 * Created by plt3ch on 6/16/2015.
 */
public class Ingredient implements Serializable {
    private String Id;
    private Product Product;
    private String Quantity;

    public Product getProduct() {
        return Product;
    }

    public void setProduct(Product product) {
        Product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
