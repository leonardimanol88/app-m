package com.example.proyect0.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FuelPriceResponse(
    val success: Boolean = false,
    val data: Map<String, CountryFuelData> = emptyMap()
)

@Serializable
data class CountryFuelData(
    @SerialName("country_name") val countryName: String? = null,
    val currency: String? = null,
    val unit: String? = null,
    val prices: FuelPrices? = null
)

@Serializable
data class FuelPrices(
    val gasoline: Double? = null,
    @SerialName("gasoline_premium") val gasolinePremium: Double? = null,
    val diesel: Double? = null,
    val premium: Double? = null
)