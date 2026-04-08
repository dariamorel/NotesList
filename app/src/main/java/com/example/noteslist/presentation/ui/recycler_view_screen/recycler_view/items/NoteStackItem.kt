package com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items

import com.example.noteslist.domain.Note

data class NoteStackItem(
    val notes: List<Note>
) : NotesItem