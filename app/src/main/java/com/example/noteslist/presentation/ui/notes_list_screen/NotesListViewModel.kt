package com.example.noteslist.presentation.ui.notes_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesListViewModel(
): ViewModel() {
    val notes = NotesRepository.notesList

    fun changeImportance(note: Note, isImportant: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                isImportant = isImportant
            )
            NotesRepository.editNote(note, updated)
        }
    }

    fun changeRead(note: Note, isRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                isRead = isRead
            )
            NotesRepository.editNote(note, updated)
        }
    }
}