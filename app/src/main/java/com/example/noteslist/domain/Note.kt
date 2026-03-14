package com.example.noteslist.domain

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class Note @JvmOverloads constructor(
    val title: String,
    val body: String,
    val isRead: Boolean = false,
    val createTime: OffsetDateTime = OffsetDateTime.now(),
    val isImportant: Boolean = false
) {
    fun getCreateTimeFormatted(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return createTime.format(formatter)
    }
}
