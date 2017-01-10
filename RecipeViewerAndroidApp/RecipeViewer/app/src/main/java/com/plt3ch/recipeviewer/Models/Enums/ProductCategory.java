package com.plt3ch.recipeviewer.Models.Enums;

/**
 * Created by Plamen on 1/9/2017.
 */

public enum ProductCategory {
    WEIGHT("grams"),
    LIQUID("liters"),
    COUNTABLE("");

    private String systemOfUnits;
    ProductCategory(String systemOfUnits) {
        this.setSystemOfUnits(systemOfUnits);
    }

    public String getSystemOfUnits() {
        return systemOfUnits;
    }

    public void setSystemOfUnits(String systemOfUnits) {
        this.systemOfUnits = systemOfUnits;
    }
}
