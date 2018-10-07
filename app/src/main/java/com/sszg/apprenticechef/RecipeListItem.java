package com.sszg.apprenticechef;

public class RecipeListItem {

    private String imageName;
    private String recipeName;
    private String Date;

    public String getImageName() {
        return imageName;
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

    public RecipeListItem(String imageName, String recipeName, String date) {
        this.imageName = imageName;

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
