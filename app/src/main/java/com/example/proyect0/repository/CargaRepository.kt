package com.example.proyect0.repository

import com.example.proyect0.data.Carga
import com.example.proyect0.data.CargaDao
import com.example.proyect0.data.Usuario
import kotlinx.coroutines.flow.Flow
import com.example.proyect0.data.SesionManager
import com.example.proyect0.data.UsuarioDao
import java.util.Calendar


class CargaRepository(private val cargaDao: CargaDao, private val sessionManager: SesionManager) {


    fun obtenerIdUsuario(): Int = sessionManager.obtenerIdUsuario()

//    val todasLasCargas: Flow<List<Carga>> = cargaDao.obtenerCargasPorUsuario()

    fun obtenerCargasDelUsuario(): Flow<List<Carga>> =
        cargaDao.obtenerCargasPorUsuario(sessionManager.obtenerIdUsuario())

    suspend fun insertar(carga: Carga) {
        cargaDao.insertarCarga(carga.copy(usuarioId = sessionManager.obtenerIdUsuario()))
    }

//    suspend fun insertar(carga: Carga) {
//        cargaDao.insertarCarga(carga)
//    }

    suspend fun borrar(carga: Carga) = cargaDao.borrar(carga)

    suspend fun contarCargasDelMes(): Int {
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val inicioMes = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        val inicioMesSiguiente = cal.timeInMillis

        return cargaDao.contarCargasEntre(
            sessionManager.obtenerIdUsuario(),
            inicioMes,
            inicioMesSiguiente
        )
    }
}