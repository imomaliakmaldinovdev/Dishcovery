package com.dishcovery.app.data.preferences

import android.content.Context
import com.dishcovery.app.domain.model.UiLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _language = MutableStateFlow(
        UiLanguage.entries.firstOrNull { it.code == preferences.getString(KEY_LANGUAGE, UiLanguage.EN.code) }
            ?: UiLanguage.EN
    )
    val language = _language.asStateFlow()
    private val _savedIds = MutableStateFlow(
        preferences.getStringSet(KEY_SAVED_IDS, emptySet()).orEmpty().mapNotNull(String::toIntOrNull).toSet()
    )
    val savedIds = _savedIds.asStateFlow()

    fun setLanguage(language: UiLanguage) {
        _language.value = language
        preferences.edit().putString(KEY_LANGUAGE, language.code).apply()
    }

    fun saveRecipe(id: Int) {
        val updated = _savedIds.value + id
        _savedIds.value = updated
        preferences.edit().putStringSet(KEY_SAVED_IDS, updated.map(Int::toString).toSet()).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "dishcovery"
        const val KEY_LANGUAGE = "ui_language"
        const val KEY_SAVED_IDS = "saved_recipe_ids"
    }
}
