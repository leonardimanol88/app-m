package com.example.proyect0.repository

import com.example.proyect0.data.Usuario
import com.example.proyect0.data.UsuarioDao

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    // el viewmodel de login llamará a esto
    suspend fun login(usuario: String, contrasena: String): Usuario? {
        return usuarioDao.iniciarSesion(usuario, contrasena)
    }

    suspend fun registrar(usuario: Usuario) {
        usuarioDao.registrarUsuario(usuario)
    }
}