package com.example.noteslist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteslist.domain.Note
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "notes_list")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val body: String,
    val isRead: Boolean,
    val createTime: String,
    val isImportant: Boolean
)

fun Note.toNotesEntity(): NotesEntity {
    return NotesEntity(
        id = this.id,
        title = this.title,
        body = this.body,
        isRead = this.isRead,
        createTime = this.createTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        ,
        isImportant = this.isImportant
    )
}