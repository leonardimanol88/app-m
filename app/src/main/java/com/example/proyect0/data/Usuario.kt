package com.example.proyect0.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// tabla usuario en la base de datos
@Entity(tableName = "usuarios_table")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreUsuario: String,
    val contrasena: String
)