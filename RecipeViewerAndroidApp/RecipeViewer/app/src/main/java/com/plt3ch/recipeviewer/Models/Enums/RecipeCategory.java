package com.plt3ch.recipeviewer.Models.Enums;

/**
 * Created by Plamen on 1/9/2017.
 */

public enum RecipeCategory {
    VEGAN_MEAL_WITH_MEAT("Vegan food with meat :)"),
    WITH_MEAT("Food with meat"),
    ONLY_MEAT("Food with only meat"),
    FOR_MEAT_LOVERS("For meat lovers - extra meat"),
    MEAT_WITH_MEAT("Sandwich meat of meat with meat"),
    MEAT_WITH_MEAT_TOPING("Meat with meat toping");

    private String friendlyName;

    RecipeCategory(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }
}
