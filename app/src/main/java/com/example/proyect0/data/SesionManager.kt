package com.example.proyect0.data

import android.content.Context

class SesionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun guardarSesion(idUsuario: Int) {
        prefs.edit().putInt("id_usuario", idUsuario).apply()
    }

    fun obtenerIdUsuario(): Int = prefs.getInt("id_usuario", -1)

    fun haySesion(): Boolean = obtenerIdUsuario() != -1

    fun cerrarSesion() {
        prefs.edit().remove("id_usuario").apply()
    }
}

