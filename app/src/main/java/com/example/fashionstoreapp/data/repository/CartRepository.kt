package com.example.fashionstoreapp.data.repository

import android.app.Application
import com.example.fashionstoreapp.data.localdatebase.AppDatabase
import com.example.fashionstoreapp.data.model.Cart
import com.example.fashionstoreapp.data.network.ApiConfig
import com.example.fashionstoreapp.data.network.api.CartApi

class CartRepository(app: Application) {
    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val cartApi: CartApi = ApiConfig.retrofit.create(CartApi::class.java)

    suspend fun getAllCart() = cartApi.getAllCart()

    suspend fun addCart(cart: Cart) = cartApi.addCart(cart)

    suspend fun updateCartQuantity(id: Int, quantity: Int) = cartApi.updateCartQuantity(id, quantity)

    suspend fun deleteCartById(id: Int) = cartApi.deleteCartById(id)

    suspend fun deleteCartByUser() = cartApi.deleteCartByUser()
}