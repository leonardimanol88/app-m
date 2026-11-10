package com.example.proyect0.data.remote

import retrofit2.http.GET

interface FuelPriceApi {
    @GET("api/fuel/prices")
    suspend fun getFuelPrices(): FuelPriceResponse
}