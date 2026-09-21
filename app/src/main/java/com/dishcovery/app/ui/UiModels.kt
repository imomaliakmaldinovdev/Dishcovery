package com.dishcovery.app.ui

import androidx.compose.ui.graphics.Color
import com.dishcovery.app.RecipeFull
import com.dishcovery.app.RecipeShort

/** Screen-safe representation of a recipe card. */
data class RecipeCardUi(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String,
    val hasVideo: Boolean,
    val isSaved: Boolean = false
)

/** Complete content rendered by the recipe-detail screen. */
data class RecipeDetailUi(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String,
    val description: String,
    val ingredients: List<IngredientUi>,
    val steps: List<RecipeStepUi>,
    val isSaved: Boolean
)

data class IngredientUi(
    val text: String,
    val isChecked: Boolean = false
)

data class RecipeStepUi(
    val number: Int,
    val label: String,
    val text: String
)

data class ProfileUi(
    val initials: String,
    val displayName: String,
    val subtitle: String,
    val stats: List<ProfileStatUi>,
    val settings: List<SettingRowUi>
)

data class ProfileStatUi(val value: String, val label: String)

data class SettingRowUi(
    val id: String,
    val title: String,
    val summary: String = "",
    val isEnabled: Boolean = true
)

data class EmptyStateUi(
    val title: String,
    val message: String,
    val actionLabel: String? = null
)

data class UiPalette(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val accent: Color
)

fun RecipeShort.toCardUi(isSaved: Boolean = false) = RecipeCardUi(
    id = id,
    title = title,
    category = category,
    imageUrl = imageUrl,
    hasVideo = hasVideo,
    isSaved = isSaved
)

fun RecipeFull.toDetailUi(isSaved: Boolean = false) = RecipeDetailUi(
    id = id,
    title = title,
    category = category,
    imageUrl = imageUrl,
    description = description,
    ingredients = ingredients.map { ingredient ->
        IngredientUi(listOfNotNull(ingredient.amount, ingredient.name).joinToString(" "))
    },
    steps = steps.mapIndexed { index, step -> RecipeStepUi(index + 1, step.label, step.text) },
    isSaved = isSaved
)
