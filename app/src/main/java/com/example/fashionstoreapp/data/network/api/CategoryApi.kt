package com.example.fashionstoreapp.data.network.api

import com.example.fashionstoreapp.data.model.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApi {

    @GET("api/v1/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("api/v1/categories/list")
    suspend fun getCategoriesList(): Response<List<Category>>

    @GET("api/v1/categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<Category>

    @GET("api/v1/categories/sub/{id}")
    suspend fun findAllSubCategoryIds(@Path("id") id: Int): Response<List<Int>>
}