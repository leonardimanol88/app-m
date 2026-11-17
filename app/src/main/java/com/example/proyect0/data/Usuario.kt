package com.example.proyect0.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios_table")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreUsuario: String,
    val contrasena: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)