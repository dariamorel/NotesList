package com.example.noteslist.presentation.ui.edit_note_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditNoteViewModel(
): ViewModel() {

    fun editNote(note: Note, newTitle: String? = null, newBody: String? = null, newIsRead: Boolean? = null, newIsImportant: Boolean? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                title = newTitle ?: note.title,
                body = newBody ?: note.body,
                isRead = newIsRead ?: note.isRead,
                isImportant = newIsImportant ?: note.isImportant
            )
            NotesRepository.editNote(note, updated)
        }
    }
}