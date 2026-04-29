package com.example.noteslist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteslist.domain.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertData(notesEntity: NotesEntity)

    @Query("SELECT * from notes_list ORDER BY createTime DESC")
    fun getNotesList(): Flow<List<NotesEntity>>

    @Update
    suspend fun updateNote(note: NotesEntity)

    @Query("DELETE from notes_list")
    suspend fun cleanDataBase()
}