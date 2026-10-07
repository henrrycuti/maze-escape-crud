package com.example.mazeescape

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Monstruo::class], version = 1)
abstract class MonstruoDatabase : RoomDatabase() {
    abstract fun monstruoDao(): MonstruoDao

    companion object {
        @Volatile
        private var instance: MonstruoDatabase? = null

        fun get(context: Context): MonstruoDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MonstruoDatabase::class.java,
                    "maze_escape_db"
                ).build().also { instance = it }
            }
    }
}
