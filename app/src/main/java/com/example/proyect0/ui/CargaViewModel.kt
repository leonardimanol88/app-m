package com.example.proyect0.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyect0.data.Carga
import com.example.proyect0.repository.CargaRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

import androidx.lifecycle.viewModelScope
import com.example.proyect0.repository.FuelPriceRepository
import com.example.proyect0.repository.PrecioGasolina
import com.example.proyect0.data.esDelMesActual
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface FuelPriceUiState {
    data object Loading : FuelPriceUiState
    data class Success(val precio: PrecioGasolina) : FuelPriceUiState
    data class Error(val mensaje: String) : FuelPriceUiState
}

class CargaViewModel(private val repository: CargaRepository, private val fuelPriceRepository: FuelPriceRepository ) : ViewModel() {
//    val todasLasCargas = repository.obtenerCargasDelUsuario()
    private val _idUsuario = MutableStateFlow(-1)
    private val _fuelPriceState = MutableStateFlow<FuelPriceUiState>(FuelPriceUiState.Loading)
    val fuelPriceState: StateFlow<FuelPriceUiState> = _fuelPriceState.asStateFlow()

    fun agregarCarga(kilometraje: Int, presupuesto: Double, litros: Double, costo: Double) {

        val usuario = repository.obtenerIdUsuario()

        // viewModelScope asegura que esto se ejecute sin congelar la app
        viewModelScope.launch {

            val nuevaCarga = Carga(
                usuarioId = usuario,
                kilometraje = kilometraje,
                presupuesto = presupuesto,
                litros = litros,
                costo = costo
            )
            repository.insertar(nuevaCarga)
        }
    }

    fun borrarCarga(carga: Carga) { viewModelScope.launch { repository.borrar(carga) } }


    fun cargarPrecioGasolina() {
        viewModelScope.launch {
            _fuelPriceState.value = FuelPriceUiState.Loading
            val resultado = fuelPriceRepository.obtenerPrecioGasolinaMexico()
            _fuelPriceState.value = resultado.fold(
                onSuccess = { FuelPriceUiState.Success(it) },
                onFailure = { FuelPriceUiState.Error(it.message ?: "Error desconocido") }
            )

        }

    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val todasLasCargas: StateFlow<List<Carga>> = _idUsuario
        .flatMapLatest { repository.obtenerCargasDelUsuario() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun actualizarUsuario() {
        _idUsuario.value = repository.obtenerIdUsuario()
    }

    val cargasEsteMes: StateFlow<Int> = todasLasCargas
        .map { lista -> lista.count { it.esDelMesActual() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

}