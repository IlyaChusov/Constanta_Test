package com.johnny.constanta_test.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceWork private constructor() {
    companion object {
        private const val PREF_SORT_ORDER = "defaultSortOrder"
        private const val APP_PREFERENCES = "appPreferences"
        private lateinit var preferences: SharedPreferences
        var sortingDesc
            get() = preferences.getBoolean(PREF_SORT_ORDER, true)
            set(isDescending) = preferences.edit().putBoolean(PREF_SORT_ORDER, isDescending).apply()

        fun init(context: Context) {
            if (!this::preferences.isInitialized)
                preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        }
    }
}