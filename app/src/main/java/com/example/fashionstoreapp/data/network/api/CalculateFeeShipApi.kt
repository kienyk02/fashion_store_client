package com.example.fashionstoreapp.data.network.api

import com.example.fashionstoreapp.data.payload.CalculateFeeShip
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface CalculateFeeShipApi {
    @POST("v2/shipping-order/fee")
    suspend fun calculateFeeShip(@Body calculateFeeShip: CalculateFeeShip): JsonObject
}