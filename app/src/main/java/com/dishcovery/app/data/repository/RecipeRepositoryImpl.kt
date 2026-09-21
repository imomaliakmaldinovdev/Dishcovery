package com.dishcovery.app.data.repository

import com.dishcovery.app.OshxonaApi
import com.dishcovery.app.RecipeFull
import com.dishcovery.app.RecipeShort
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.model.RecipeIngredient
import com.dishcovery.app.domain.model.RecipeInstruction
import com.dishcovery.app.domain.repository.RecipeRepository

class RecipeRepositoryImpl(private val api: OshxonaApi) : RecipeRepository {
    override suspend fun getRecipes(page: Int, perPage: Int) = api.recipes(page = page, perPage = perPage).items.map(RecipeShort::toDomain)
    override suspend fun searchRecipes(query: String) = api.search(query).items.map(RecipeShort::toDomain)
    override suspend fun getRecipe(id: Int) = api.recipe(id).toDomain()
    override suspend fun getRecipesByIds(ids: Set<Int>) = ids.mapNotNull { id ->
        runCatching { getRecipe(id) }.getOrNull()
    }
    override suspend fun getRecipeCount() = api.recipes(perPage = 1).total
}

private fun RecipeShort.toDomain() = Recipe(id, title, category, imageUrl, hasVideo)
private fun RecipeFull.toDomain() = Recipe(id, title, category, imageUrl, false, description, ingredients.map { RecipeIngredient(it.amount, it.name) }, steps.map { RecipeInstruction(it.label, it.text) })
