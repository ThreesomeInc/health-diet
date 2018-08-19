package com.blackchicktech.healthdiet.domain;

import com.blackchicktech.healthdiet.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeListItem {

    @JsonProperty("recipeId")
    private String recipeId;

    @JsonProperty("recipeName")
    private String recipeName;

    @JsonProperty("recipeUrl")
    private String picUrl;

    public RecipeListItem(Recipe recipe) {
        this.recipeId = recipe.getRecipeId();
        this.recipeName = recipe.getRecipeName();
        this.picUrl = recipeId + ".pic";
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "RecipeListItem{" +
                "recipeId='" + recipeId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
