package com.example.noteslist.presentation.ui.add_note_screen

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