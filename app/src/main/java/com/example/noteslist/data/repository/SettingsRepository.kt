package com.example.noteslist.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.noteslist.R

class SettingsRepository(
    context: Context
) {
    private val defaultStackSpacing = context.resources.getDimension(R.dimen.default_stack_spacing)
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    init {
        if (prefs.contains(FIRST_LOAD_NAME)) {
            prefs.edit {
                putBoolean(FIRST_LOAD_NAME, false)
            }
        } else {
            prefs.edit {
                putBoolean(FIRST_LOAD_NAME, true)
            }
        }
    }

    fun getIsFirstLoad(): Boolean {
        return prefs.getBoolean(FIRST_LOAD_NAME, true)
    }
    fun firstLoadDone() {
        prefs.edit {
            putBoolean(FIRST_LOAD_NAME, false)
        }
    }

    fun getStackSpacingCurrent(): Float {
        return prefs.getFloat(STACK_SPACING_NAME, defaultStackSpacing)
    }

    fun getStackMaxVisibleCurrent(): Int {
        return prefs.getInt(STACK_MAX_VISIBLE_NAME, DEFAULT_STACK_MAX_VISIBLE)
    }

    suspend fun setStackSpacing(value: Float) {
        prefs.edit {
            putFloat(STACK_SPACING_NAME, value)
        }
    }

    suspend fun setStackMaxVisible(value: Int) {
        prefs.edit {
            putInt(STACK_MAX_VISIBLE_NAME, value)
        }
    }

    companion object {
        const val PREFERENCES_NAME = "app_prefs"
        const val STACK_SPACING_NAME = "stack_spacing"
        const val STACK_MAX_VISIBLE_NAME = "stack_max_visible"
        const val DEFAULT_STACK_MAX_VISIBLE = 4
        const val FIRST_LOAD_NAME = "first_load"
    }
}