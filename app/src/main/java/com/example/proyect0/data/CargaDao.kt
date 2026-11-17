package com.example.proyect0.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CargaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCarga(carga: Carga): Long

    @Delete
    suspend fun borrar(carga: Carga)

//    @Query("SELECT * FROM cargas_table ORDER BY id DESC")
//    fun obtenerTodasLasCargas(): Flow<List<Carga>>

    @Query("SELECT * FROM cargas_table WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun obtenerCargasPorUsuario(usuarioId: Int): Flow<List<Carga>>

    @Query("""
    SELECT COUNT(*) FROM cargas_table
    WHERE usuarioId = :usuarioId AND fecha >= :desde AND fecha < :hasta
    """)
    suspend fun contarCargasEntre(usuarioId: Int, desde: Long, hasta: Long): Int

}