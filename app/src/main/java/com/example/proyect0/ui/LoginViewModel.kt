package com.example.proyect0.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect0.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    //  variable que guardará el estado del login para que la Vista reaccione
    // inactivo, cargando exito o error
    private val _mensajeLogin = MutableStateFlow("")
    val mensajeLogin: StateFlow<String> = _mensajeLogin.asStateFlow()

    fun verificarCredenciales(usuario: String, contrasena: String) {
        if (usuario.isBlank() || contrasena.isBlank()) {
            _mensajeLogin.value = "Por favor, llena todos los campos"
            return
        }


        viewModelScope.launch {
            _mensajeLogin.value = "Verificando..."

            val usuarioEncontrado = repository.login(usuario, contrasena)

            if (usuarioEncontrado != null) {
                _mensajeLogin.value = "¡Bienvenido, ${usuarioEncontrado.nombreUsuario}!"
            } else {
                _mensajeLogin.value = "Usuario o contraseña incorrectos"
            }
        }
    }


    fun registrarUsuario(usuario: String, contrasena: String) {
        if (usuario.isBlank() || contrasena.isBlank()) {
            _mensajeLogin.value = "Por favor, llena los campos para registrarte"
            return
        }

        viewModelScope.launch {
            _mensajeLogin.value = "Registrando..."

            val nuevoUsuario = com.example.proyect0.data.Usuario(
                nombreUsuario = usuario,
                contrasena = contrasena
            )

            repository.registrar(nuevoUsuario)

            _mensajeLogin.value = "Registro exitoso ,Ya puedes iniciar sesión."
        }
    }

}