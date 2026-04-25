package com.example.noteslist.presentation.ui.add_note_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(
): ViewModel() {

    var title by mutableStateOf("")
    var body by mutableStateOf("")
    var isImportant by mutableStateOf(false)
    var isTitleFilled by mutableStateOf(false)
    var showTitleError by mutableStateOf(false)

    fun onTitleChanged(newTitle: String) {
        title = newTitle
        if (newTitle.isBlank()) {
            isTitleFilled = false
        } else {
            isTitleFilled = true
            if (showTitleError) {
                showTitleError = false
            }
        }
    }

    fun onBodyChanged(newBody: String) {
        body = newBody
    }

    fun toggleImportance() {
        isImportant = !isImportant
    }

    fun onShowTitleErrorChanged(newShowTitleError: Boolean) {
        showTitleError = newShowTitleError
    }

    fun addNewNote(title: String, body: String, isImportant: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            NotesRepository.addNote(
                Note(
                    title = title,
                    body = body,
                    isImportant = isImportant
                )
            )
        }
    }
}