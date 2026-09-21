package com.dishcovery.app.di

import android.content.Context
import com.dishcovery.app.OshxonaService
import com.dishcovery.app.data.preferences.UserPreferencesRepository
import com.dishcovery.app.data.repository.RecipeRepositoryImpl
import com.dishcovery.app.domain.repository.RecipeRepository
import com.dishcovery.app.domain.usecase.GetRecipeCountUseCase
import com.dishcovery.app.domain.usecase.GetRecipeUseCase
import com.dishcovery.app.domain.usecase.GetRecipesUseCase
import com.dishcovery.app.domain.usecase.GetSavedRecipesUseCase
import com.dishcovery.app.domain.usecase.SearchRecipesUseCase

class AppContainer(context: Context) {
    private val recipeRepository: RecipeRepository = RecipeRepositoryImpl(OshxonaService.api)
    val userPreferences = UserPreferencesRepository(context)
    val getRecipes = GetRecipesUseCase(recipeRepository)
    val searchRecipes = SearchRecipesUseCase(recipeRepository)
    val getRecipe = GetRecipeUseCase(recipeRepository)
    val getSavedRecipes = GetSavedRecipesUseCase(recipeRepository)
    val getRecipeCount = GetRecipeCountUseCase(recipeRepository)
}
