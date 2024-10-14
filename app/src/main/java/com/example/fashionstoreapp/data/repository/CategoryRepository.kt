package com.example.fashionstoreapp.data.repository

import com.example.fashionstoreapp.data.model.Category
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.CategoryApi
import retrofit2.Response

class CategoryRepository {

    private val categoryApi= ApiConfig.retrofit.create(CategoryApi::class.java)

    suspend fun getCategories() = categoryApi.getCategories();

    suspend fun getCategoryById(id: Int) = categoryApi.getCategoryById(id);

    suspend fun findAllSubCategoryIds(id: Int) = categoryApi.findAllSubCategoryIds(id);

}