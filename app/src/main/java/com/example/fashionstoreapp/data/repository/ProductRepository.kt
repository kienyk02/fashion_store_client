package com.example.fashionstoreapp.data.repository

import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.ProductApi
import retrofit2.Response

class ProductRepository {

    private val productApi = ApiConfig.retrofit.create(ProductApi::class.java)

    suspend fun getProducts() = productApi.getProducts();

    suspend fun getProductById(id: Int) = productApi.getProductById(id);

    suspend fun searchProducts(keyword: String) = productApi.searchProducts(keyword);

    suspend fun getProductsByCategory(id: Int) = productApi.getProductsByCategory(id)

}