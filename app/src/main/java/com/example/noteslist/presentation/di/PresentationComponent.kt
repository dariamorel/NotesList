package com.example.noteslist.presentation.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteViewModel
import com.example.noteslist.presentation.ui.edit_note_screen.EditNoteViewModel
import com.example.noteslist.presentation.ui.notes_list_screen.NotesListViewModel

class PresentationComponent(
    private val dependencies: Dependencies
) {
    fun createNotesListViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer { createNotesListViewModel() }
        }
    }

    fun createAddNoteViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer { createAddNoteViewModel() }
        }
    }

    fun createEditNoteViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer { createEditNoteViewModel() }
        }
    }

    private fun createNotesListViewModel(): NotesListViewModel {
        return NotesListViewModel(
            repository = dependencies.getNotesRepository()
        )
    }

    private fun createAddNoteViewModel(): AddNoteViewModel {
        return AddNoteViewModel(
            repository = dependencies.getNotesRepository()
        )
    }

    private fun createEditNoteViewModel(): EditNoteViewModel {
        return EditNoteViewModel(
            repository = dependencies.getNotesRepository()
        )
    }

    interface Dependencies {
        fun getNotesRepository(): NotesRepository
    }
}