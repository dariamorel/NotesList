package com.example.noteslist.domain.recyclerView

import com.example.noteslist.domain.Note

data class NoteStackItem(
    val notes: List<Note>
) : NotesItem