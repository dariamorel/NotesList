package com.example.noteslist.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteslist.data.db.DataBase
import com.example.noteslist.data.db.DataBaseManager
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.data.repository.SettingsRepository

class DataComponent(
    private val dependencies: Dependencies
) {
    private val notesRepositoryValue by lazy { createNotesRepository() }

    private val dataBase by lazy { createDataBase() }

    private val dataBaseManager by lazy { createDataBaseManager() }

    private val settingsRepositoryValue by lazy { createSettingsRepository() }

    fun getNotesRepository(): NotesRepository {
        return notesRepositoryValue
    }

    fun getSettingsRepository(): SettingsRepository {
        return settingsRepositoryValue
    }

    private fun createSettingsRepository(): SettingsRepository {
        return SettingsRepository(dependencies.getApplicationContext())
    }

    private fun createNotesRepository(): NotesRepository {
        return NotesRepository(dataBaseManager)
    }

    private fun createDataBaseManager(): DataBaseManager {
        return DataBaseManager(dataBase)
    }

    private fun createDataBase(): DataBase {
        return Room.databaseBuilder(
            dependencies.getApplicationContext(),
            DataBase::class.java,
            "notes.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("""
                    INSERT INTO notes_list (title, body, isRead, createTime, isImportant)
                    VALUES 
                        ('Погулять с собакой', 'Сходить в парк и на игровую площадку', 0, '2026-03-12T20:00:00+03:00', 0),
                        ('Сделать курсач', 'Созвониться с научником, доделать важные задачи', 0, '2026-03-12T21:20:00+03:00', 1),
                        ('Бильярд', 'Встреча в 18:00 на Охотном ряду', 0, '2026-03-12T21:05:00+03:00', 0),
                        ('Купить пылесос', 'Зайти в м видео и выбрать пылесос', 1, '2026-03-12T22:00:00+03:00', 0),
                        ('Сходить за продуктами', 'Список: молоко, чай, кофе, вафли', 0, '2026-03-13T15:23:00+03:00', 1),
                        ('Врач', 'Среда 15:30', 1, '2026-03-13T16:00:00+03:00', 0),
                        ('Доделать курсач', 'Доделать уже наконец-то курсач', 0, '2026-03-13T17:00:00+03:00', 0),
                        ('Йога', 'Йога с собаками в парке в 15:00 сб', 1, '2026-03-13T18:00:00+03:00', 0),
                        ('Список в поездку', 'Фотик, ноутбук, одежда, кроссовки', 1, '2026-03-13T19:00:00+03:00', 0)
                """)
                }
            })
            .build()
    }


    interface Dependencies {
        fun getApplicationContext(): Context
    }
}