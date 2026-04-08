package com.example.noteslist.presentation.recycler_view.items

import com.example.noteslist.domain.Note

data class NoteStackItem(
    val notes: List<Note>
) : NotesItem