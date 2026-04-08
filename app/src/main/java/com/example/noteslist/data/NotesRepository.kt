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
            isImportant = true,
            isRead = false
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
            createTime = OffsetDateTime.of(2026, 3, 13, 15, 23, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = true
        ),
        Note(
            title = "Врач",
            body = "Среда 15:30",
            createTime = OffsetDateTime.of(2026, 3, 13, 16, 0, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = false,
            isRead = true
        ),
        Note(
            title = "Доделать курсач",
            body = "Доделать уже наконец-то курсач, доделать уже наконец-то курсач",
            createTime = OffsetDateTime.of(2026, 3, 13, 17, 0, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = false,
            isRead = false
        ),
        Note(
            title = "Йога",
            body = "Йога с собаками в парке в 15:00 сб",
            createTime = OffsetDateTime.of(2026, 3, 13, 18, 0, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = false,
            isRead = true
        ),
        Note(
            title = "Список в поездку",
            body = "Фотик, ноутбук, одежда, кроссовки",
            createTime = OffsetDateTime.of(2026, 3, 13, 19, 0, 0, 0, ZoneOffset.ofHours(3)),
            isImportant = false,
            isRead = true
        ),
    )
    val notesList: List<Note>
        get() = _notesList.toList()

    fun addNote(note: Note) {
        _notesList.add(note)
    }

    fun editNote(note: Note, newNote: Note) {
        _notesList.remove(note)
        _notesList.add(newNote)
    }
}