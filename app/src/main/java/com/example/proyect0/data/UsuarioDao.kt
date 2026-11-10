package com.example.proyect0.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    // guardar insertar nuevo usuario
    @Insert
    suspend fun registrarUsuario(usuario: Usuario): Long


    @Query("SELECT * FROM usuarios_table WHERE nombreUsuario = :usuario AND contrasena = :pass")
    suspend fun iniciarSesion(usuario: String, pass: String): Usuario?
}