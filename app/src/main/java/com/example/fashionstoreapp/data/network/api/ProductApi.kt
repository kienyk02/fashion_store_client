package com.example.fashionstoreapp.data.network.api

import com.example.fashionstoreapp.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("api/v1/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(@Query("id") id: Int): Response<Product>

    @GET("api/v1/products/search/{keyword}")
    suspend fun searchProducts(@Path("keyword") keyword: String): Response<List<Product>>

    @GET("api/v1/products/category/{id}")
    suspend fun getProductsByCategory(
        @Path("id") id: Int
    ): Response<List<Product>>
}