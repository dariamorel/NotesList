package com.example.noteslist.data.repository

import com.example.noteslist.data.db.DataBase
import com.example.noteslist.data.db.DataBaseManager
import com.example.noteslist.data.db.toNotesEntity
import com.example.noteslist.domain.Note
import com.example.noteslist.domain.toNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime
import java.time.ZoneOffset

class NotesRepository(
    private val dataBaseManager: DataBaseManager
) {
    val notesList = dataBaseManager.getNotesList().map { list ->
        list.map { entity ->
            entity.toNote()
        }
    }

    suspend fun addNote(note: Note) {
        dataBaseManager.insertData(note.toNotesEntity())
    }

    suspend fun editNote(newNote: Note) {
        dataBaseManager.updateNote(newNote.toNotesEntity())
    }
}