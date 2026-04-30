package com.example.noteslist.data.db

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.noteslist.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DataBaseManager(
    private val dataBase: DataBase
) {

    suspend fun insertData(entity: NotesEntity) {
        dataBase.dataBaseDao.insertData(entity)
    }

    fun getNotesList(): Flow<List<NotesEntity>> {
        return dataBase.dataBaseDao.getNotesList()
    }

    suspend fun updateNote(entity: NotesEntity) {
        dataBase.dataBaseDao.updateNote(entity)
    }

    suspend fun clearDataBase() {
        dataBase.dataBaseDao.cleanDataBase()
    }
}