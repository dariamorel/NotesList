package com.example.noteslist.domain

import android.os.Parcelable
import com.example.noteslist.data.db.NotesEntity
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Note @JvmOverloads constructor(
    val id: Long = 0,
    val title: String,
    val body: String,
    val isRead: Boolean = false,
    val createTime: OffsetDateTime = OffsetDateTime.now(),
    val isImportant: Boolean = false
): Parcelable {
    fun getCreateTimeFormatted(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return createTime.format(formatter)
    }
}

fun NotesEntity.toNote(): Note {
    return Note(
        id = this.id,
        title = this.title,
        body = this.body,
        isRead = this.isRead,
        createTime = OffsetDateTime.parse(this.createTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        isImportant = this.isImportant
    )
}
