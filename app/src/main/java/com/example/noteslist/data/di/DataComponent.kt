package com.example.noteslist.data.di

import com.example.noteslist.data.repository.NotesRepository

class DataComponent() {
    private val repository by lazy { NotesRepository() }
    fun getNotesRepository(): NotesRepository {
        return repository
    }
}