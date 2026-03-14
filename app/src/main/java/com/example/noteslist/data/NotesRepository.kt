package com.example.noteslist.data

import com.example.noteslist.domain.Note
import java.time.OffsetDateTime
import java.time.ZoneOffset

object NotesRepository {
    private var _notesList = mutableListOf(
        Note(
            title = "Погулять с собакой",
            body = "Сходить в парк и на игровую площадку",
            createTime = OffsetDateTime.of(2026, 3, 12, 20, 0, 0, 0, ZoneOffset.ofHours(3))
        ),
        Note(
            title = "Сделать курсач",
            body = "Созвониться с научником, доделать важные задачи, подготовить презентацию, подправить код в проекте",
            createTime = OffsetDateTime.of(2026, 3, 12, 21, 20, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = true
        ),
        Note(
            title = "Бильярд",
            body = "Встреча в 18:00 на Охотном ряду",
            createTime = OffsetDateTime.of(2026, 3, 12, 21, 5, 0, 0, ZoneOffset.ofHours(3))
        ),
        Note(
            title = "Купить пылесос",
            body = "Зайти в м видео и выбрать пылесос",
            createTime = OffsetDateTime.of(2026, 3, 12, 22, 0, 0, 0, ZoneOffset.ofHours(3)),
            isRead = true
        ),
        Note(
            title = "Сходить за продуктами",
            body = "Список: молоко, чай, кофе, вафли",
            createTime = OffsetDateTime.of(2026, 3, 13, 15, 23, 0, 0, ZoneOffset.ofHours(3))
        ),
        Note(
            title = "Врач",
            body = "Среда 15:30",
            createTime = OffsetDateTime.of(2026, 3, 13, 16, 0, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = true
        )
    )
    val notesList = _notesList.toList()

    fun addNote(note: Note) {
        _notesList.add(note)
    }

    fun changeRead(note: Note) {
        val isRead = note.isRead
        _notesList.remove(note)
        _notesList.add(note.copy(isRead = !isRead))
    }

    fun changeImportance(note: Note) {
        val isImportant = note.isImportant
        _notesList.remove(note)
        _notesList.add(note.copy(isImportant = !isImportant))
    }
}