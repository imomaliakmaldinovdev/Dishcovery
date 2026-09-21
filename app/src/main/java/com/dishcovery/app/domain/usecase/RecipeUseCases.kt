package com.dishcovery.app.domain.usecase

import com.dishcovery.app.domain.repository.RecipeRepository

class GetRecipesUseCase(private val repository: RecipeRepository) { suspend operator fun invoke() = repository.getRecipes() }
class SearchRecipesUseCase(private val repository: RecipeRepository) { suspend operator fun invoke(query: String) = repository.searchRecipes(query) }
class GetRecipeUseCase(private val repository: RecipeRepository) { suspend operator fun invoke(id: Int) = repository.getRecipe(id) }
class GetSavedRecipesUseCase(private val repository: RecipeRepository) { suspend operator fun invoke(ids: Set<Int>) = repository.getRecipesByIds(ids) }
class GetRecipeCountUseCase(private val repository: RecipeRepository) { suspend operator fun invoke() = repository.getRecipeCount() }
