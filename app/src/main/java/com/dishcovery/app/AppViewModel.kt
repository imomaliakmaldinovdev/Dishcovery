package com.dishcovery.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dishcovery.app.domain.model.UiLanguage

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = (application as DishcoveryApplication).container.userPreferences
    val language = preferences.language
    val savedIds = preferences.savedIds
    fun setLanguage(value: UiLanguage) = preferences.setLanguage(value)
    fun saveRecipe(id: Int) = preferences.saveRecipe(id)
}
