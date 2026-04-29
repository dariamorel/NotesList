package com.example.noteslist.presentation.ui.notes_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesListViewModel(
    private val repository: NotesRepository
): ViewModel() {
    val notes = repository.notesList

    fun changeImportance(note: Note, isImportant: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                isImportant = isImportant
            )
            repository.editNote(note, updated)
        }
    }

    fun changeRead(note: Note, isRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                isRead = isRead
            )
            repository.editNote(note, updated)
        }
    }
}