package com.example.mazeescape

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monstruo")
data class Monstruo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val velocidad: Int
)
