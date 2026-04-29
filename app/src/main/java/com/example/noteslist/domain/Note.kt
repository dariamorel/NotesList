package com.example.noteslist.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Note @JvmOverloads constructor(
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
