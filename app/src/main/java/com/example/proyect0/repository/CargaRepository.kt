package com.example.proyect0.repository

import com.example.proyect0.data.Carga
import com.example.proyect0.data.CargaDao
import kotlinx.coroutines.flow.Flow


class CargaRepository(private val cargaDao: CargaDao) {


    val todasLasCargas: Flow<List<Carga>> = cargaDao.obtenerTodasLasCargas()

    suspend fun insertar(carga: Carga) {
        cargaDao.insertarCarga(carga)
    }

    suspend fun borrar(carga: Carga) = cargaDao.borrar(carga)
}