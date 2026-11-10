package com.example.proyect0.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cargas_table")

data class Carga(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kilometraje: Int,
    val presupuesto: Double,
    val litros: Double,
    val costo: Double,
    val fecha: Long = System.currentTimeMillis()
)