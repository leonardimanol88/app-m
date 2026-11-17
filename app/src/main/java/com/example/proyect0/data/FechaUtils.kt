package com.example.proyect0.data

import java.util.Calendar

fun Carga.esDelMesActual(): Boolean {
    val hoy = Calendar.getInstance()
    val fecha = Calendar.getInstance().apply { timeInMillis = this@esDelMesActual.fecha }
    return hoy.get(Calendar.MONTH) == fecha.get(Calendar.MONTH) &&
            hoy.get(Calendar.YEAR) == fecha.get(Calendar.YEAR)
}