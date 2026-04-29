package com.example.noteslist.presentation.ui.notes_list_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.data.repository.SettingsRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesListViewModel(
    private val notesRepository: NotesRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    val notes = notesRepository.notesList

    private val searchQuery = MutableStateFlow("")

    val searchResults: StateFlow<List<Note>> = combine(
        notes,
        searchQuery
    ) { allNotes, query ->
        if (query.isBlank()) {
            allNotes
        } else {
            allNotes.filter { note ->
                note.title.contains(query, ignoreCase = true)
            }
        }
    }
        .debounce(300)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun clearSearch() {
        searchQuery.value = ""
    }

    fun getIsFirstLoad(): Boolean {
        return settingsRepository.getIsFirstLoad()
    }
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