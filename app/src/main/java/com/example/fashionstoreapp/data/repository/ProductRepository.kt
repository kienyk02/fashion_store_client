package com.example.fashionstoreapp.data.repository

import com.example.fashionstoreapp.data.model.Product
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.ProductApi
import retrofit2.Response

class ProductRepository {

    private val productApi = ApiConfig.retrofit.create(ProductApi::class.java)

    suspend fun getProducts() = productApi.getProducts();

    suspend fun getProducts(page: Int, limit: Int) = productApi.getProducts(page, limit);

    suspend fun getProductById(id: Int) = productApi.getProductById(id);

    suspend fun searchProducts(
        keyword: String,
        page: Int,
        limit: Int
    ) = productApi.searchProducts(keyword, page, limit);

    suspend fun getProductsBestSeller() = productApi.getProductsBestSeller()

    suspend fun getProductsBestDiscount() = productApi.getProductsBestDiscount()

    suspend fun getProductsByCategory(id: Int) = productApi.getProductsByCategory(id)

    suspend fun getProductsByCategory(id: Int, page: Int, limit: Int) =
        productApi.getProductsByCategory(id, page, limit)

}