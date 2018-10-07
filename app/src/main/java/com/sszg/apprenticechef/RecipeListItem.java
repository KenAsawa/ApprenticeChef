package com.sszg.apprenticechef;

public class RecipeListItem {

    private String imageName;
    private String recipeName;
    private String Date;
    private String ingredientsList;

    public String getImageName() {
        return imageName;
    }

    public String getIngredientsList() {
        return ingredientsList;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public RecipeListItem(String imageName, String recipeName, String date, String ingredientsList) {
        this.imageName = imageName;
        this.ingredientsList = ingredientsList;
        this.recipeName = recipeName;

        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
