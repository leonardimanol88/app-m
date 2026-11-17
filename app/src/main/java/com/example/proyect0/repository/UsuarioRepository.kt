package com.example.proyect0.repository

import com.example.proyect0.data.Usuario
import com.example.proyect0.data.UsuarioDao
import com.example.proyect0.data.SesionManager

class UsuarioRepository(private val usuarioDao: UsuarioDao,  private val sessionManager: SesionManager) {
    suspend fun login(usuario: String, contrasena: String): Usuario? {
        val encontrado = usuarioDao.iniciarSesion(usuario, contrasena)
        if (encontrado != null) {
            sessionManager.guardarSesion(encontrado.id)
        }
        return encontrado
    }

    suspend fun registrar(usuario: Usuario) {
        usuarioDao.registrarUsuario(usuario)
    }

    suspend fun obtenerUsuarioActual(): Usuario? {
        val id = sessionManager.obtenerIdUsuario()
        return if (id == -1) null else usuarioDao.obtenerPorId(id)
    }

    fun cerrarSesion() {
        sessionManager.cerrarSesion()
    }
}