package com.example.mazeescape

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MonstruoDao {
    // READ: la lista se actualiza sola cuando cambia la tabla
    @Query("SELECT * FROM monstruo ORDER BY id DESC")
    fun getAll(): Flow<List<Monstruo>>

    // CREATE
    @Insert
    suspend fun insert(monstruo: Monstruo)

    // UPDATE
    @Update
    suspend fun update(monstruo: Monstruo)

    // DELETE
    @Delete
    suspend fun delete(monstruo: Monstruo)
}
