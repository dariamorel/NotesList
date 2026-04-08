package com.example.noteslist.presentation.recycler_view.items

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DateItem(
    val date: LocalDate
): NotesItem {
    fun getDateFormatted(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(formatter)
    }
}