package com.example.noteslist.domain

import java.time.OffsetDateTime

data class Note @JvmOverloads constructor(
    val title: String,
    val body: String,
    val isRead: Boolean = false,
    val createTime: OffsetDateTime = OffsetDateTime.now(),
    val isImportant: Boolean = false
)
