package com.example.noteslist.presentation.ui.settings_screen

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.data.repository.SettingsRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
): ViewModel() {

    var stackSpacing by mutableStateOf(repository.getStackSpacingCurrent().toString())
    var stackMaxVisible by mutableStateOf(repository.getStackMaxVisibleCurrent().toString())

    fun onStackSpacingChanged(newStackSpacing: String) {
        stackSpacing = newStackSpacing
    }

    fun onStackMaxVisibleChanged(newStackMaxVisible: String) {
        stackMaxVisible = newStackMaxVisible
    }

    fun saveSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val stackSpacingFloat = stackSpacing.toFloatOrNull()
            stackSpacingFloat?.let {
                repository.setStackSpacing(stackSpacingFloat)
            }
            val stackMaxVisibleInt = stackMaxVisible.toIntOrNull()
            stackMaxVisibleInt?.let {
                repository.setStackMaxVisible(stackMaxVisibleInt)
            }
        }
    }
}