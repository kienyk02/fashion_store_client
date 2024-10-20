package com.example.fashionstoreapp.data.network.api

import androidx.annotation.RequiresApi
import com.example.fashionstoreapp.data.model.Cart
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CartApi {
    @GET("api/carts")
    suspend fun getAllCart(): Response<List<Cart>>

    @POST("api/add-cart")
    suspend fun addCart(@Body cart: Cart): Response<Cart>

    @POST("api/cart/update/{id}")
    suspend fun updateCartQuantity(@Path("id") id: Int, @Query("quantity") quantity: Int)

    @DELETE("api/cart/delete/{id}")
    suspend fun deleteCartById(@Path("id") id: Int)

    @DELETE("api/cart/delete-all")
    suspend fun deleteCartByUser()
}