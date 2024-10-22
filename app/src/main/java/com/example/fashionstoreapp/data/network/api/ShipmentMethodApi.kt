package com.example.fashionstoreapp.data.network.api

import com.example.fashionstoreapp.data.model.ShipmentMethod
import retrofit2.http.GET

interface ShipmentMethodApi {
    @GET("api/v1/shipmentmethods")
    suspend fun getShipmentMethods(): List<ShipmentMethod>
}