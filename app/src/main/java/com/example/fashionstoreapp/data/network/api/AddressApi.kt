package com.example.fashionstoreapp.data.network.api

import com.example.fashionstoreapp.data.model.Address
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AddressApi {
    @GET("api/v1/address")
    suspend fun getAllAddress(): Response<List<Address>>

    @POST("api/v1/address")
    suspend fun saveAddress(@Body address: Address): Response<Address>


    @DELETE("api/v1/address/{id}")
    suspend fun deleteAddressById(@Path("id") id: Int)
}