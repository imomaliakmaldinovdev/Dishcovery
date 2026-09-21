package com.dishcovery.app.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.DishcoveryApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

sealed interface AsyncUiState<out T> {
    data object Idle : AsyncUiState<Nothing>
    data object Loading : AsyncUiState<Nothing>
    data class Success<T>(val data: T) : AsyncUiState<T>
    data class Error(val message: String) : AsyncUiState<Nothing>
}

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    private val getRecipes = (application as DishcoveryApplication).container.getRecipes
    private val _state = MutableStateFlow<AsyncUiState<List<Recipe>>>(AsyncUiState.Idle)
    val state = _state.asStateFlow()
    fun load() = viewModelScope.launch { _state.value = AsyncUiState.Loading; _state.value = runCatching { AsyncUiState.Success(getRecipes()) }.getOrElse { AsyncUiState.Error("Couldn’t load recipes") } }
}

class RecipeSearchViewModel(application: Application) : AndroidViewModel(application) {
    private val searchRecipes = (application as DishcoveryApplication).container.searchRecipes
    private val _state = MutableStateFlow<AsyncUiState<List<Recipe>>>(AsyncUiState.Idle)
    val state = _state.asStateFlow()
    private var searchJob: Job? = null
    fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.length < 2) { _state.value = AsyncUiState.Idle; return@launch }
            _state.value = AsyncUiState.Loading
            try {
                _state.value = AsyncUiState.Success(searchRecipes(query))
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _state.value = AsyncUiState.Error("Search is unavailable")
            }
        }
    }
}

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val getRecipe = (application as DishcoveryApplication).container.getRecipe
    private val _state = MutableStateFlow<AsyncUiState<Recipe>>(AsyncUiState.Idle)
    val state = _state.asStateFlow()
    fun load(id: Int) = viewModelScope.launch { _state.value = AsyncUiState.Loading; _state.value = runCatching { AsyncUiState.Success(getRecipe(id)) }.getOrElse { AsyncUiState.Error("Recipe unavailable") } }
}

class SavedRecipesViewModel(application: Application) : AndroidViewModel(application) {
    private val getSavedRecipes = (application as DishcoveryApplication).container.getSavedRecipes
    private val _state = MutableStateFlow<AsyncUiState<List<Recipe>>>(AsyncUiState.Idle)
    val state = _state.asStateFlow()
    fun load(ids: Set<Int>) = viewModelScope.launch {
        if (ids.isEmpty()) { _state.value = AsyncUiState.Idle; return@launch }
        _state.value = AsyncUiState.Loading
        _state.value = runCatching { AsyncUiState.Success(getSavedRecipes(ids)) }
            .getOrElse { AsyncUiState.Error("Couldn’t load saved recipes") }
    }
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val getRecipeCount = (application as DishcoveryApplication).container.getRecipeCount
    private val _recipeCount = MutableStateFlow<Int?>(null)
    val recipeCount = _recipeCount.asStateFlow()
    fun load() = viewModelScope.launch { _recipeCount.value = runCatching { getRecipeCount() }.getOrNull() }
}
