package com.example.noteslist.presentation.ui.notes_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.data.repository.SettingsRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesListViewModel(
    private val notesRepository: NotesRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    val notes = notesRepository.notesList

    fun changeImportance(note: Note, isImportant: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                isImportant = isImportant
            )
            notesRepository.editNote(updated)
        }
    }

    fun changeRead(note: Note, isRead: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = note.copy(
                isRead = isRead
            )
            notesRepository.editNote(updated)
        }
    }

    fun getStackSpacingCurrent(): Float {
        return settingsRepository.getStackSpacingCurrent()
    }

    fun getStackMaxVisibleCurrent(): Int {
        return settingsRepository.getStackMaxVisibleCurrent()
    }
}