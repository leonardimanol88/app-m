package com.example.proyect0.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect0.repository.CargaRepository
import com.example.proyect0.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PerfilUiState(
    val nombreUsuario: String = "",
    val fechaRegistro: String = "",
    val cargasDelMes: Int = 0,
    val cargando: Boolean = true
)

class PerfilViewModel(private val repository: UsuarioRepository, private val cargaRepository: CargaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    fun cargarPerfil() {
        viewModelScope.launch {
            _uiState.value = PerfilUiState(cargando = true)

            val usuario = repository.obtenerUsuarioActual()
            val cargasDelMes = cargaRepository.contarCargasDelMes()

            _uiState.value = if (usuario != null) {
                PerfilUiState(
                    nombreUsuario = usuario.nombreUsuario,
                    fechaRegistro = formatearFecha(usuario.fechaRegistro),
                    cargasDelMes = cargasDelMes,
                    cargando = false
                )
            } else {
                PerfilUiState(cargando = false)
            }
        }
    }

    fun cerrarSesion() {
        repository.cerrarSesion()
        _uiState.value = PerfilUiState()
    }

    private fun formatearFecha(millis: Long): String {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(Date(millis))
    }
}