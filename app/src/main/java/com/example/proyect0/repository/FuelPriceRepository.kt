package com.example.proyect0.repository

import com.example.proyect0.data.remote.FuelPriceApi
import com.example.proyect0.data.remote.FuelPrices


data class PrecioGasolina(
    val precioPorLitro: Double,
    val moneda: String,
    val unidad: String,
    val premium: Double?
)

class FuelPriceRepository(private val api: FuelPriceApi) {

    suspend fun obtenerPrecioGasolinaMexico(): Result<PrecioGasolina> {
        return try {
            val response = api.getFuelPrices()
            val pais = response.data["MX"]
            val precio = pais?.prices?.gasoline

            if (response.success && precio != null && precio > 0) {
                Result.success(
                    PrecioGasolina(
                        precioPorLitro = precio,
                        moneda = pais.currency ?: "MXN",
                        unidad = pais.unit ?: "litro",
                        premium = pais.prices.gasolinePremium
                    )
                )
            } else {
                Result.failure(Exception("Sin datos válidos de gasolina para México"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}