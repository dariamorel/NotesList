package com.example.noteslist

import android.app.Application
import com.example.noteslist.presentation.view_model.NotesViewModel

class Application: Application() {
    private var _notesViewModel: NotesViewModel? = null
    val notesViewModel
        get() = _notesViewModel!!

    override fun onCreate() {
        super.onCreate()
        _notesViewModel = NotesViewModel()
    }
}