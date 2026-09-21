package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Recipe

interface RecipeRepository {
    suspend fun getRecipes(page: Int = 0, perPage: Int = 20): List<Recipe>
    suspend fun searchRecipes(query: String): List<Recipe>
    suspend fun getRecipe(id: Int): Recipe
    suspend fun getRecipesByIds(ids: Set<Int>): List<Recipe>
    suspend fun getRecipeCount(): Int
}
