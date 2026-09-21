package com.dishcovery.app.data.repository

import com.dishcovery.app.Ingredient
import com.dishcovery.app.OshxonaApi
import com.dishcovery.app.RecipeFull
import com.dishcovery.app.RecipePage
import com.dishcovery.app.RecipeShort
import com.dishcovery.app.RecipeStep
import com.dishcovery.app.SearchResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeRepositoryImplTest {
    private val repository = RecipeRepositoryImpl(FakeApi())

    @Test fun `recipe list maps API recipe to domain recipe`() = runBlocking {
        val recipe = repository.getRecipes().single()

        assertEquals(7, recipe.id)
        assertEquals("Plov", recipe.title)
        assertEquals("uzbek", recipe.category)
        assertEquals(true, recipe.hasVideo)
    }

    @Test fun `recipe detail maps ingredients and instructions`() = runBlocking {
        val recipe = repository.getRecipe(7)

        assertEquals("A classic dish", recipe.description)
        assertEquals("250 g", recipe.ingredients.single().amount)
        assertEquals("Cook rice", recipe.steps.single().text)
    }

    private class FakeApi : OshxonaApi {
        private val short = RecipeShort(7, "Plov", "uzbek", "https://example.test/plov.jpg", true)
        override suspend fun recipes(language: String, page: Int, perPage: Int) = RecipePage(listOf(short), 1)
        override suspend fun search(query: String, language: String) = SearchResponse(listOf(short))
        override suspend fun recipe(id: Int) = RecipeFull(
            id = id,
            title = "Plov",
            description = "A classic dish",
            category = "uzbek",
            imageUrl = "https://example.test/plov.jpg",
            ingredients = listOf(Ingredient("250 g", "rice")),
            steps = listOf(RecipeStep("Step 1", "Cook rice"))
        )
    }
}
