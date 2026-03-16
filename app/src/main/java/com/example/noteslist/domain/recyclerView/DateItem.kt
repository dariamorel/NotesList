package com.example.noteslist.domain.recyclerView

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class DateItem(
    val date: LocalDate
): NotesItem {
    fun getDateFormatted(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(formatter)
    }
}
