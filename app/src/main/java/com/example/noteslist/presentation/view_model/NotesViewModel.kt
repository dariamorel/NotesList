package com.example.noteslist.presentation.view_model

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(listOf())
    val notes = _notes.asStateFlow()

    init {
        _notes.value = NotesRepository.notesList
    }
    fun addNewNote(title: String, body: String?, isImportant: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            NotesRepository.addNote(
                Note(
                    title = title,
                    body = body,
                    isImportant = isImportant
                )
            )
            _notes.value = NotesRepository.notesList
        }
    }
}