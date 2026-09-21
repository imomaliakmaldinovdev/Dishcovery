package com.dishcovery.app.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String,
    val hasVideo: Boolean,
    val description: String = "",
    val ingredients: List<RecipeIngredient> = emptyList(),
    val steps: List<RecipeInstruction> = emptyList()
)

data class RecipeIngredient(val amount: String?, val name: String?)
data class RecipeInstruction(val label: String, val text: String)
