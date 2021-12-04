package com.daironiq.recipeapp.services;

import com.daironiq.recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}
